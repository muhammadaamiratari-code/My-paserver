"""VidForge render worker."""

from pathlib import Path
import subprocess
from database.connection import get_connection


def render_job(job_id):
    output = Path("storage/final") / f"job_{job_id}.mp4"
    output.parent.mkdir(parents=True, exist_ok=True)

    subprocess.run([
        "ffmpeg", "-y",
        "-f", "lavfi",
        "-i", "color=c=black:s=1280x720:r=24",
        "-t", "1",
        "-c:v", "libx264",
        "-pix_fmt", "yuv420p",
        str(output),
    ], check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

    db = get_connection()
    db.execute("UPDATE jobs SET status = 'COMPLETED', completed_at = CURRENT_TIMESTAMP, result_path = ? WHERE id = ? AND status = 'GENERATING'", (str(output), job_id))
    db.commit()
    db.close()

    return {"job_id": job_id, "status": "COMPLETED", "result_path": str(output)}
