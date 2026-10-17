from fastapi import FastAPI, Request, HTTPException
from pathlib import Path
from uuid import uuid4
from pydantic import BaseModel
from database.connection import get_connection

app = FastAPI(title="VidForge API")

@app.post("/api/reference-images")
async def upload_reference_image(request: Request):
    content_type = request.headers.get("content-type", "").split(";")[0].strip().lower()
    extensions = {
        "image/jpeg": ".jpg",
        "image/png": ".png",
        "image/webp": ".webp",
        "image/gif": ".gif",
    }

    if content_type not in extensions:
        raise HTTPException(status_code=415, detail="Unsupported image type.")

    content_length = request.headers.get("content-length")
    if content_length and int(content_length) > 15 * 1024 * 1024:
        raise HTTPException(status_code=413, detail="Image exceeds 15 MB limit.")

    image_data = await request.body()
    if not image_data:
        raise HTTPException(status_code=400, detail="Image is empty.")
    if len(image_data) > 15 * 1024 * 1024:
        raise HTTPException(status_code=413, detail="Image exceeds 15 MB limit.")

    filename = f"{uuid4().hex}{extensions[content_type]}"
    image_dir = Path(__file__).resolve().parents[2] / "storage" / "assets" / "reference_images"
    image_dir.mkdir(parents=True, exist_ok=True)
    (image_dir / filename).write_bytes(image_data)

    return {"reference_image_path": f"storage/assets/reference_images/{filename}"}


@app.get("/api/health")
def health():
    return {"status": "ok", "service": "vidforge"}


@app.get("/api/users/count")
def users_count():
    db = get_connection()
    count = db.execute("SELECT COUNT(*) AS n FROM users").fetchone()["n"]
    db.close()
    return {"users": count}


class ProjectCreate(BaseModel):
    user_id: int
    title: str

@app.post("/api/projects")
def create_project(data: ProjectCreate):
    db = get_connection()
    cursor = db.execute("INSERT INTO projects (user_id, title) VALUES (?, ?)", (data.user_id, data.title))
    db.commit()
    project = db.execute("SELECT id, user_id, title, status FROM projects WHERE id = ?", (cursor.lastrowid,)).fetchone()
    db.close()
    return dict(project)


@app.get("/api/jobs/{job_id}")
def get_job(job_id: int):
    db = get_connection()
    job = db.execute("SELECT id, project_id, user_id, status, priority, prompt, duration_seconds, style, completed_at, result_path FROM jobs WHERE id = ?", (job_id,)).fetchone()
    db.close()
    if job is None:
        return {"error": "job_not_found"}
    return dict(job)


@app.get("/api/jobs")
def list_jobs(user_id: int):
    if user_id <= 0:
        raise HTTPException(status_code=400, detail="Invalid user_id")

    db = get_connection()
    try:
        rows = db.execute(
            """
            SELECT id, project_id, user_id, status, priority, created_at,
                   completed_at, result_path, retry_count, prompt,
                   duration_seconds, style, platform, aspect_ratio,
                   resolution, reference_image_path
            FROM jobs
            WHERE user_id = ?
            ORDER BY id DESC
            """,
            (user_id,)
        ).fetchall()
        return {"jobs": [dict(row) for row in rows]}
    finally:
        db.close()


class JobCreate(BaseModel):
    project_id: int
    user_id: int
    priority: str = "NORMAL"
    prompt: str = ""
    duration_seconds: int = 5
    style: str = "default"
    platform: str = "TikTok"
    aspect_ratio: str = "9:16"
    resolution: str = "720p"
    reference_image_path: str | None = None


def ensure_video_job_columns(db):
    existing = {
        row["name"] for row in db.execute("PRAGMA table_info(jobs)").fetchall()
    }
    additions = {
        "prompt": "TEXT",
        "duration_seconds": "INTEGER",
        "style": "TEXT",
        "platform": "TEXT NOT NULL DEFAULT 'TikTok'",
        "aspect_ratio": "TEXT NOT NULL DEFAULT '9:16'",
        "resolution": "TEXT NOT NULL DEFAULT '720p'",
        "reference_image_path": "TEXT",
    }
    for name, definition in additions.items():
        if name not in existing:
            db.execute(f"ALTER TABLE jobs ADD COLUMN {name} {definition}")
    db.commit()

@app.post("/api/jobs")
def create_job(data: JobCreate):
    db = get_connection()
    ensure_video_job_columns(db)
    cursor = db.execute(
        "INSERT INTO jobs (project_id, user_id, priority, prompt, duration_seconds, style, platform, aspect_ratio, resolution, reference_image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        (data.project_id, data.user_id, data.priority, data.prompt, data.duration_seconds, data.style, data.platform, data.aspect_ratio, data.resolution, data.reference_image_path)
    )
    db.commit()
    job = db.execute("SELECT id, project_id, user_id, status, priority, prompt, duration_seconds, style, platform, aspect_ratio, resolution, reference_image_path FROM jobs WHERE id = ?", (cursor.lastrowid,)).fetchone()
    db.close()
    return dict(job)

from urllib.parse import urlencode
from urllib.request import urlopen
import json

GOOGLE_WEB_CLIENT_ID = "857038606651-dg27bvhgd2pclltl63nubb13ungdhdqn.apps.googleusercontent.com"


class GoogleAuthRequest(BaseModel):
    id_token: str


@app.post("/api/auth/google")
def google_auth(data: GoogleAuthRequest):
    print("AUTH STEP 1: /api/auth/google received", flush=True)
    try:
        query = urlencode({"id_token": data.id_token})
        with urlopen(
            "https://oauth2.googleapis.com/tokeninfo?" + query,
            timeout=10
        ) as response:
            info = json.loads(response.read().decode("utf-8"))
            print("AUTH STEP 2: Google tokeninfo response received", flush=True)
    except Exception:
        return {"error": "invalid_google_token"}

    if info.get("aud") != GOOGLE_WEB_CLIENT_ID:
        return {"error": "invalid_google_audience"}

    provider_subject = info.get("sub")
    name = info.get("name") or "VidForge User"

    if not provider_subject:
        return {"error": "google_subject_missing"}

    print("AUTH STEP 3: Opening database", flush=True)
    db = get_connection()
    db.execute("PRAGMA busy_timeout = 5000")
    print("AUTH STEP 4: Database opened", flush=True)

    try:
        print("AUTH STEP 5: Checking auth identity", flush=True)
        identity = db.execute(
            """
            SELECT user_id
            FROM auth_identities
            WHERE provider = ? AND provider_subject = ?
            """,
            ("google", provider_subject)
        ).fetchone()

        if identity:
            print("AUTH STEP 6: Existing identity found", flush=True)
            user_id = identity["user_id"]
        else:
            print("AUTH STEP 6: Identity not found, creating user", flush=True)
            cursor = db.execute(
                "INSERT INTO users (name) VALUES (?)",
                (name,)
            )
            user_id = cursor.lastrowid

            print("AUTH STEP 7: User created, inserting identity", flush=True)
            db.execute(
                """
                INSERT INTO auth_identities
                (user_id, provider, provider_subject)
                VALUES (?, ?, ?)
                """,
                (user_id, "google", provider_subject)
            )

        db.commit()
        print("AUTH STEP 8: Database commit complete", flush=True)
    finally:
        db.close()

    print("AUTH STEP 9: Sending connected response", flush=True)
    return {
        "user_id": user_id,
        "provider": "google",
        "name": name,
        "status": "connected"
    }
from fastapi import HTTPException


class PlanningRequest(BaseModel):
    user_request: str
    duration_seconds: int = 5
    style: str = "cinematic"
    characters: list[dict] = []
    audio_requested: bool = True


@app.post("/api/planning/plan")
def create_video_plan(data: PlanningRequest):
    try:
        result = video_planning_workflow.invoke({
            "user_request": data.user_request,
            "duration_seconds": data.duration_seconds,
            "style": data.style,
            "characters": data.characters,
            "audio_requested": data.audio_requested,
            "errors": [],
        })
    except ModuleNotFoundError as exc:
        if exc.name == "langgraph" or (exc.name and exc.name.startswith("langgraph.")):
            raise HTTPException(
                status_code=503,
                detail="Video planning is unavailable because LangGraph is not installed."
            ) from exc
        raise
    except (ValueError, TypeError) as exc:
        raise HTTPException(status_code=400, detail=str(exc))

    return {
        "status": result.get("status"),
        "understood_request": result.get("understood_request"),
        "script": result.get("script"),
        "storyboard": result.get("storyboard"),
        "continuity_report": result.get("continuity_report"),
        "prompts": result.get("prompts"),
        "audio_plan": result.get("audio_plan"),
        "errors": result.get("errors", []),
    }
