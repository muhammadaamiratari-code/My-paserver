"""
اے آئی اسسٹنٹ
LOCAL DEVELOPER MEMORY ENGINE
"""

import os
import json
import sqlite3
import hashlib
import tempfile
from pathlib import Path
from datetime import datetime, timezone
from contextlib import contextmanager

APP_NAME = "اے آئی اسسٹنٹ"
DATABASE_NAME = "project_memory.db"
BACKUP_FOLDER_NAME = "backups"
MAX_BACKUPS = 10
SCHEMA_VERSION = 1


def utc_now():
    return datetime.now(timezone.utc).isoformat()


def _storage_root():
    configured = os.environ.get("AI_ASSISTANT_PRIVATE_STORAGE", "").strip()
    root = Path(configured).expanduser().resolve() if configured else Path.home() / ".ai_assistant"
    folder = root / "local_developer_memory"
    folder.mkdir(parents=True, exist_ok=True)
    return folder


MEMORY_FOLDER = _storage_root()
MEMORY_DB = MEMORY_FOLDER / DATABASE_NAME
BACKUP_FOLDER = MEMORY_FOLDER / BACKUP_FOLDER_NAME


def connect():
    conn = sqlite3.connect(str(MEMORY_DB), timeout=30, isolation_level=None)
    conn.row_factory = sqlite3.Row
    conn.execute("PRAGMA foreign_keys=ON")
    conn.execute("PRAGMA journal_mode=WAL")
    conn.execute("PRAGMA synchronous=FULL")
    conn.execute("PRAGMA busy_timeout=30000")
    return conn


@contextmanager
def transaction():
    conn = connect()
    try:
        conn.execute("BEGIN IMMEDIATE")
        yield conn
        conn.execute("COMMIT")
    except Exception:
        try:
            conn.execute("ROLLBACK")
        except Exception:
            pass
        raise
    finally:
        conn.close()


def initialize_database():
    MEMORY_FOLDER.mkdir(parents=True, exist_ok=True)
    BACKUP_FOLDER.mkdir(parents=True, exist_ok=True)
    conn = connect()
    try:
        conn.executescript("""
        CREATE TABLE IF NOT EXISTS metadata (key TEXT PRIMARY KEY, value TEXT NOT NULL);
        CREATE TABLE IF NOT EXISTS projects (
            id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE,
            description TEXT NOT NULL DEFAULT '', status TEXT NOT NULL DEFAULT 'ACTIVE',
            progress INTEGER NOT NULL DEFAULT 0, current_goal TEXT NOT NULL DEFAULT '',
            current_task TEXT NOT NULL DEFAULT '', next_action TEXT NOT NULL DEFAULT '',
            pause_reason TEXT NOT NULL DEFAULT '', created_at TEXT NOT NULL,
            updated_at TEXT NOT NULL, last_worked_at TEXT, paused_at TEXT
        );
        CREATE TABLE IF NOT EXISTS files (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL,
            path TEXT NOT NULL, purpose TEXT NOT NULL DEFAULT '', status TEXT NOT NULL DEFAULT 'ACTIVE',
            content_hash TEXT NOT NULL DEFAULT '', last_changed_at TEXT, notes TEXT NOT NULL DEFAULT '',
            UNIQUE(project_id, path), FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS tasks (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, title TEXT NOT NULL,
            description TEXT NOT NULL DEFAULT '', status TEXT NOT NULL DEFAULT 'PENDING',
            priority TEXT NOT NULL DEFAULT 'NORMAL', created_at TEXT NOT NULL, updated_at TEXT NOT NULL,
            completed_at TEXT, FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS tests (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, name TEXT NOT NULL,
            purpose TEXT NOT NULL DEFAULT '', status TEXT NOT NULL DEFAULT 'PENDING', result TEXT NOT NULL DEFAULT '',
            failure_reason TEXT NOT NULL DEFAULT '', last_tested_at TEXT, code_state_hash TEXT NOT NULL DEFAULT '',
            notes TEXT NOT NULL DEFAULT '', created_at TEXT NOT NULL, updated_at TEXT NOT NULL,
            UNIQUE(project_id, name), FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS failures (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, title TEXT NOT NULL,
            description TEXT NOT NULL DEFAULT '', attempted_solution TEXT NOT NULL DEFAULT '', result TEXT NOT NULL DEFAULT '',
            status TEXT NOT NULL DEFAULT 'OPEN', created_at TEXT NOT NULL, resolved_at TEXT,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS decisions (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, title TEXT NOT NULL,
            decision TEXT NOT NULL, reason TEXT NOT NULL DEFAULT '', status TEXT NOT NULL DEFAULT 'APPROVED',
            created_at TEXT NOT NULL, updated_at TEXT NOT NULL,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS changes (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, file_path TEXT NOT NULL DEFAULT '',
            summary TEXT NOT NULL, reason TEXT NOT NULL DEFAULT '', result TEXT NOT NULL DEFAULT '', created_at TEXT NOT NULL,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS sessions (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, summary TEXT NOT NULL DEFAULT '',
            work_done TEXT NOT NULL DEFAULT '', work_remaining TEXT NOT NULL DEFAULT '', stopped_at TEXT NOT NULL DEFAULT '',
            next_action TEXT NOT NULL DEFAULT '', created_at TEXT NOT NULL,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS logs (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER NOT NULL, category TEXT NOT NULL DEFAULT 'GENERAL',
            message TEXT NOT NULL, level TEXT NOT NULL DEFAULT 'INFO', created_at TEXT NOT NULL,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS memory_items (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER, category TEXT NOT NULL,
            title TEXT NOT NULL, content TEXT NOT NULL, created_at TEXT NOT NULL, updated_at TEXT NOT NULL,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE TABLE IF NOT EXISTS chats (
            id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER, role TEXT NOT NULL,
            content TEXT NOT NULL, created_at TEXT NOT NULL,
            FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
        );
        CREATE INDEX IF NOT EXISTS idx_files_project ON files(project_id);
        CREATE INDEX IF NOT EXISTS idx_tasks_project ON tasks(project_id);
        CREATE INDEX IF NOT EXISTS idx_tests_project ON tests(project_id);
        CREATE INDEX IF NOT EXISTS idx_failures_project ON failures(project_id);
        CREATE INDEX IF NOT EXISTS idx_decisions_project ON decisions(project_id);
        CREATE INDEX IF NOT EXISTS idx_changes_project ON changes(project_id);
        CREATE INDEX IF NOT EXISTS idx_sessions_project ON sessions(project_id);
        CREATE INDEX IF NOT EXISTS idx_logs_project ON logs(project_id);
        CREATE INDEX IF NOT EXISTS idx_memory_project ON memory_items(project_id);
        CREATE INDEX IF NOT EXISTS idx_chats_project ON chats(project_id);
        """)
        conn.execute("INSERT OR REPLACE INTO metadata (key, value) VALUES (?, ?)", ("schema_version", str(SCHEMA_VERSION)))
        active = conn.execute("SELECT value FROM metadata WHERE key='active_project_id'").fetchone()
        if active is None:
            first = conn.execute("SELECT id FROM projects ORDER BY id LIMIT 1").fetchone()
            if first is None:
                now = utc_now()
                cur = conn.execute("INSERT INTO projects (name, description, status, progress, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)", ("Default Project", "Default local project.", "ACTIVE", 0, now, now))
                project_id = cur.lastrowid
            else:
                project_id = first["id"]
            conn.execute("INSERT OR REPLACE INTO metadata (key, value) VALUES ('active_project_id', ?)", (str(project_id),))
    finally:
        conn.close()


initialize_database()


def _dict(row):
    return dict(row) if row else None


def list_projects():
    conn = connect()
    try:
        return [_dict(row) for row in conn.execute("SELECT * FROM projects ORDER BY updated_at DESC").fetchall()]
    finally:
        conn.close()


def get_project(project_id=None, project_name=None):
    conn = connect()
    try:
        if project_id is not None:
            row = conn.execute("SELECT * FROM projects WHERE id=?", (project_id,)).fetchone()
        elif project_name is not None:
            row = conn.execute("SELECT * FROM projects WHERE name=?", (str(project_name).strip(),)).fetchone()
        else:
            return None
        return _dict(row)
    finally:
        conn.close()


def get_active_project():
    conn = connect()
    try:
        row = conn.execute("SELECT value FROM metadata WHERE key='active_project_id'").fetchone()
        if not row:
            return None
        return _dict(conn.execute("SELECT * FROM projects WHERE id=?", (int(row["value"]),)).fetchone())
    finally:
        conn.close()


def create_new_project(project_name, description=""):
    name = str(project_name).strip()
    if not name:
        return False, "پروجیکٹ کا نام خالی نہیں ہو سکتا۔"
    if get_project(project_name=name):
        return False, "اس نام کا پروجیکٹ پہلے سے موجود ہے۔"
    now = utc_now()
    try:
        with transaction() as conn:
            cur = conn.execute("INSERT INTO projects (name, description, status, progress, created_at, updated_at) VALUES (?, ?, 'ACTIVE', 0, ?, ?)", (name, str(description), now, now))
            project_id = cur.lastrowid
            conn.execute("INSERT OR REPLACE INTO metadata (key, value) VALUES ('active_project_id', ?)", (str(project_id),))
        return True, f"پروجیکٹ '{name}' کامیابی سے بن گیا ہے۔"
    except sqlite3.IntegrityError:
        return False, "اس نام کا پروجیکٹ پہلے سے موجود ہے۔"


def set_active_project(project_id):
    project = get_project(project_id=project_id)
    if not project:
        return False, "پروجیکٹ نہیں ملا۔"
    with transaction() as conn:
        conn.execute("INSERT OR REPLACE INTO metadata (key, value) VALUES ('active_project_id', ?)", (str(project_id),))
    return True, f"Active project: {project['name']}"


def update_project_state(project_id, status=None, progress=None, current_goal=None, current_task=None, next_action=None, pause_reason=None):
    if not get_project(project_id=project_id):
        return False
    fields, values = [], []
    for column, value in (("status", status), ("current_goal", current_goal), ("current_task", current_task), ("next_action", next_action), ("pause_reason", pause_reason)):
        if value is not None:
            fields.append(f"{column}=?")
            values.append(str(value))
    if progress is not None:
        try:
            progress = max(0, min(100, int(progress)))
        except (TypeError, ValueError):
            return False
        fields.append("progress=?")
        values.append(progress)
    fields.append("updated_at=?")
    values.extend((utc_now(), project_id))
    with transaction() as conn:
        conn.execute(f"UPDATE projects SET {', '.join(fields)} WHERE id=?", values)
    return True


def calculate_file_hash(file_path):
    digest = hashlib.sha256()
    path = Path(file_path)
    if not path.is_file():
        raise FileNotFoundError(str(path))
    with path.open("rb") as f:
        for chunk in iter(lambda: f.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def upsert_file(project_id, path, purpose="", status="ACTIVE", content_hash="", notes=""):
    now = utc_now()
    with transaction() as conn:
        conn.execute("""INSERT INTO files (project_id, path, purpose, status, content_hash, last_changed_at, notes)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT(project_id, path) DO UPDATE SET purpose=excluded.purpose, status=excluded.status,
        content_hash=excluded.content_hash, last_changed_at=excluded.last_changed_at, notes=excluded.notes""",
        (project_id, str(path), str(purpose), str(status), str(content_hash), now, str(notes)))
    return True


def list_project_files(project_id):
    conn = connect()
    try:
        return [_dict(row) for row in conn.execute("SELECT * FROM files WHERE project_id=? ORDER BY path", (project_id,)).fetchall()]
    finally:
        conn.close()


def add_task(project_id, title, description="", priority="NORMAL"):
    title = str(title).strip()
    if not title:
        raise ValueError("Task title cannot be empty.")
    now = utc_now()
    with transaction() as conn:
        cur = conn.execute("INSERT INTO tasks (project_id, title, description, status, priority, created_at, updated_at) VALUES (?, ?, ?, 'PENDING', ?, ?, ?)", (project_id, title, str(description), str(priority), now, now))
    return cur.lastrowid


def update_task(task_id, status=None, description=None, priority=None):
    fields, values = [], []
    if status is not None:
        fields.append("status=?"); values.append(str(status))
    if description is not None:
        fields.append("description=?"); values.append(str(description))
    if priority is not None:
        fields.append("priority=?"); values.append(str(priority))
    if not fields:
        return False
    fields.append("updated_at=?"); values.append(utc_now())
    if status is not None and str(status).upper() == "COMPLETED":
        fields.append("completed_at=?"); values.append(utc_now())
    values.append(task_id)
    with transaction() as conn:
        conn.execute(f"UPDATE tasks SET {', '.join(fields)} WHERE id=?", values)
    return True


def list_tasks(project_id, status=None):
    conn = connect()
    try:
        if status:
            rows = conn.execute("SELECT * FROM tasks WHERE project_id=? AND status=? ORDER BY updated_at DESC", (project_id, str(status))).fetchall()
        else:
            rows = conn.execute("SELECT * FROM tasks WHERE project_id=? ORDER BY updated_at DESC", (project_id,)).fetchall()
        return [_dict(row) for row in rows]
    finally:
        conn.close()


def add_test(project_id, test_name, purpose="", code_state_hash=""):
    name = str(test_name).strip()
    if not name:
        raise ValueError("Test name cannot be empty.")
    now = utc_now()
    with transaction() as conn:
        conn.execute("""INSERT INTO tests (project_id, name, purpose, status, code_state_hash, created_at, updated_at)
        VALUES (?, ?, ?, 'PENDING', ?, ?, ?)
        ON CONFLICT(project_id, name) DO UPDATE SET purpose=excluded.purpose, code_state_hash=excluded.code_state_hash, updated_at=excluded.updated_at""",
        (project_id, name, str(purpose), str(code_state_hash), now, now))
        return conn.execute("SELECT id FROM tests WHERE project_id=? AND name=?", (project_id, name)).fetchone()["id"]


def record_test_result(test_id, result, status="PASSED", failure_reason="", code_state_hash="", notes=""):
    now = utc_now()
    with transaction() as conn:
        conn.execute("UPDATE tests SET result=?, status=?, failure_reason=?, last_tested_at=?, code_state_hash=?, notes=?, updated_at=? WHERE id=?", (str(result), str(status).upper(), str(failure_reason), now, str(code_state_hash), str(notes), now, test_id))
    return True


def test_needs_retest(test_id, current_code_state_hash):
    conn = connect()
    try:
        test = conn.execute("SELECT * FROM tests WHERE id=?", (test_id,)).fetchone()
        if not test or str(test["status"]).upper() != "PASSED" or not test["last_tested_at"]:
            return True
        return str(test["code_state_hash"]) != str(current_code_state_hash)
    finally:
        conn.close()


def add_test_log(test_name, result, status="PASSED"):
    project = get_active_project()
    if not project:
        return False
    test_id = add_test(project["id"], test_name)
    record_test_result(test_id, result, status=status)
    return True


def add_failure(project_id, title, description="", attempted_solution="", result=""):
    now = utc_now()
    with transaction() as conn:
        cur = conn.execute("INSERT INTO failures (project_id, title, description, attempted_solution, result, status, created_at) VALUES (?, ?, ?, ?, ?, 'OPEN', ?)", (project_id, str(title), str(description), str(attempted_solution), str(result), now))
    return cur.lastrowid


def resolve_failure(failure_id, result=""):
    with transaction() as conn:
        conn.execute("UPDATE failures SET status='RESOLVED', result=?, resolved_at=? WHERE id=?", (str(result), utc_now(), failure_id))
    return True


def add_decision(project_id, title, decision, reason="", status="APPROVED"):
    now = utc_now()
    with transaction() as conn:
        cur = conn.execute("INSERT INTO decisions (project_id, title, decision, reason, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)", (project_id, str(title), str(decision), str(reason), str(status), now, now))
    return cur.lastrowid


def record_change(project_id, summary, file_path="", reason="", result=""):
    with transaction() as conn:
        cur = conn.execute("INSERT INTO changes (project_id, file_path, summary, reason, result, created_at) VALUES (?, ?, ?, ?, ?, ?)", (project_id, str(file_path), str(summary), str(reason), str(result), utc_now()))
    return cur.lastrowid


def save_session(project_id, summary="", work_done="", work_remaining="", stopped_at="", next_action=""):
    now = utc_now()
    with transaction() as conn:
        cur = conn.execute("INSERT INTO sessions (project_id, summary, work_done, work_remaining, stopped_at, next_action, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)", (project_id, str(summary), str(work_done), str(work_remaining), str(stopped_at), str(next_action), now))
        conn.execute("UPDATE projects SET last_worked_at=?, paused_at=?, next_action=?, updated_at=? WHERE id=?", (now, str(stopped_at) if stopped_at else None, str(next_action), now, project_id))
    return cur.lastrowid


def get_latest_session(project_id):
    conn = connect()
    try:
        return _dict(conn.execute("SELECT * FROM sessions WHERE project_id=? ORDER BY id DESC LIMIT 1", (project_id,)).fetchone())
    finally:
        conn.close()


def add_log(project_id, message, category="GENERAL", level="INFO"):
    with transaction() as conn:
        cur = conn.execute("INSERT INTO logs (project_id, category, message, level, created_at) VALUES (?, ?, ?, ?, ?)", (project_id, str(category), str(message), str(level), utc_now()))
    return cur.lastrowid


def add_memory_item(category, title, content, project_id=None):
    now = utc_now()
    with transaction() as conn:
        cur = conn.execute("INSERT INTO memory_items (project_id, category, title, content, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)", (project_id, str(category), str(title), str(content), now, now))
    return cur.lastrowid


def get_memory_item_content(item_id, item_type="chat"):
    conn = connect()
    try:
        table = "chats" if item_type == "chat" else "memory_items"
        row = conn.execute(f"SELECT content FROM {table} WHERE id=?", (item_id,)).fetchone()
        return row["content"] if row else None
    finally:
        conn.close()


def add_chat_message(project_id, role, content):
    with transaction() as conn:
        cur = conn.execute("INSERT INTO chats (project_id, role, content, created_at) VALUES (?, ?, ?, ?)", (project_id, str(role), str(content), utc_now()))
    return cur.lastrowid


def list_project_chats(project_id, limit=100):
    limit = max(1, min(int(limit), 1000))
    conn = connect()
    try:
        rows = conn.execute("SELECT * FROM chats WHERE project_id=? ORDER BY id DESC LIMIT ?", (project_id, limit)).fetchall()
        rows.reverse()
        return [_dict(row) for row in rows]
    finally:
        conn.close()


def _require_user_confirmation(user_confirmation):
    if user_confirmation is not True:
        raise PermissionError("یہ destructive memory operation صرف واضح user confirmation کے بعد چل سکتی ہے۔")


def delete_memory_item(memory_id, user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        conn.execute("DELETE FROM memory_items WHERE id=?", (memory_id,))
    return True


def delete_bulk_memory_items(item_ids_list, item_type="chats", user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    if not isinstance(item_ids_list, list) or not item_ids_list:
        return False
    valid_tables = {"chats": "chats", "memory_items": "memory_items", "tests": "tests", "logs": "logs", "tasks": "tasks"}
    target_table = valid_tables.get(item_type, "chats")
    placeholders = ",".join(["?"] * len(item_ids_list))
    with transaction() as conn:
        conn.execute(f"DELETE FROM {target_table} WHERE id IN ({placeholders})", item_ids_list)
    return True


def delete_test(test_id, user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        conn.execute("DELETE FROM tests WHERE id=?", (test_id,))
    return True


def delete_log(log_id, user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        conn.execute("DELETE FROM logs WHERE id=?", (log_id,))
    return True


def delete_task(task_id, user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        conn.execute("DELETE FROM tasks WHERE id=?", (task_id,))
    return True


def clear_project_chat(project_id, user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        conn.execute("DELETE FROM chats WHERE project_id=?", (project_id,))
    return True


def clear_project_memory(project_id, user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        for table in ("memory_items", "tests", "failures", "decisions", "changes", "sessions", "logs", "chats", "tasks"):
            conn.execute(f"DELETE FROM {table} WHERE project_id=?", (project_id,))
    return True


def clear_all_ai_memory(user_confirmation=False):
    _require_user_confirmation(user_confirmation)
    with transaction() as conn:
        for table in ("memory_items", "tests", "failures", "decisions", "changes", "sessions", "logs", "chats", "tasks"):
            conn.execute(f"DELETE FROM {table}")
    return True


def get_project_context(project_id):
    project = get_project(project_id=project_id)
    if not project:
        return None
    conn = connect()
    try:
        def rows(table, order="id DESC"):
            return [_dict(row) for row in conn.execute(f"SELECT * FROM {table} WHERE project_id=? ORDER BY {order}", (project_id,)).fetchall()]
        latest = conn.execute("SELECT * FROM sessions WHERE project_id=? ORDER BY id DESC LIMIT 1", (project_id,)).fetchone()
        chats = conn.execute("SELECT * FROM chats WHERE project_id=? ORDER BY id DESC LIMIT 50", (project_id,)).fetchall()
        chats = list(reversed([_dict(row) for row in chats]))
        return {
            "project": project,
            "files": rows("files", "path"),
            "tasks": rows("tasks", "updated_at DESC"),
            "tests": rows("tests", "updated_at DESC"),
            "failures": rows("failures", "id DESC"),
            "decisions": rows("decisions", "updated_at DESC"),
            "changes": rows("changes", "id DESC"),
            "logs": rows("logs", "id DESC"),
            "memory_items": rows("memory_items", "updated_at DESC"),
            "latest_session": _dict(latest),
            "recent_chats": chats,
        }
    finally:
        conn.close()


def create_backup():
    BACKUP_FOLDER.mkdir(parents=True, exist_ok=True)
    timestamp = datetime.now().strftime("%Y%m%d_%H%M%S_%f")
    backup_path = BACKUP_FOLDER / f"project_memory_backup_{timestamp}.db"
    source = connect(); destination = None
    try:
        destination = sqlite3.connect(str(backup_path))
        source.backup(destination); destination.commit()
        return True, str(backup_path)
    except Exception:
        try: backup_path.unlink(missing_ok=True)
        except Exception: pass
        return False, None
    finally:
        if destination: destination.close()
        source.close()


def cleanup_old_backups(max_backups=MAX_BACKUPS):
    try:
        max_backups = max(1, int(max_backups))
        backups = [p for p in BACKUP_FOLDER.iterdir() if p.is_file() and p.name.startswith("project_memory_backup_") and p.suffix == ".db"]
        backups.sort(key=lambda p: p.stat().st_mtime, reverse=True)
        for old in backups[max_backups:]:
            try: old.unlink()
            except Exception: pass
        return True
    except Exception:
        return False


def backup_and_cleanup():
    success, path = create_backup()
    cleanup_old_backups()
    return success, path


def check_integrity():
    conn = connect()
    try:
        row = conn.execute("PRAGMA integrity_check").fetchone()
        detail = row[0] if row else "unknown"
        return detail == "ok", detail
    finally:
        conn.close()


def export_memory_json(output_path):
    output_path = Path(output_path).expanduser().resolve()
    output_path.parent.mkdir(parents=True, exist_ok=True)
    conn = connect(); temp_name = None
    try:
        result = {"exported_at": utc_now(), "schema_version": SCHEMA_VERSION, "projects": []}
        projects = conn.execute("SELECT * FROM projects ORDER BY id").fetchall()
        tables = ("files", "tasks", "tests", "failures", "decisions", "changes", "sessions", "logs", "memory_items", "chats")
        for project in projects:
            pid = project["id"]; item = {"project": _dict(project)}
            for table in tables:
                item[table] = [_dict(row) for row in conn.execute(f"SELECT * FROM {table} WHERE project_id=?", (pid,)).fetchall()]
            result["projects"].append(item)
        with tempfile.NamedTemporaryFile(mode="w", encoding="utf-8", dir=str(output_path.parent), prefix=".project_memory_", suffix=".tmp", delete=False) as temp:
            json.dump(result, temp, ensure_ascii=False, indent=2); temp.flush(); os.fsync(temp.fileno()); temp_name = temp.name
        os.replace(temp_name, output_path)
        return True
    except Exception:
        if temp_name:
            try: Path(temp_name).unlink(missing_ok=True)
            except Exception: pass
        raise
    finally:
        conn.close()


if __name__ == "__main__":
    ok, detail = check_integrity()
    print("Local Developer Memory initialized.")
    print(f"Database: {MEMORY_DB}")
    print(f"Backup folder: {BACKUP_FOLDER}")
    print(f"Integrity: {'OK' if ok else detail}")
    active = get_active_project()
    if active:
        print(f"Active project: {active['name']}")
