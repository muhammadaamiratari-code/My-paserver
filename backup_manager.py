import json
import os
import shutil
from datetime import datetime

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
BACKUP_FOLDER = os.path.join(PROJECT_ROOT, "project_backups")
BACKUP_RECORD = os.path.join(
    PROJECT_ROOT,
    "backup_record.json"
)

MAX_BACKUPS = 20

os.makedirs(BACKUP_FOLDER, exist_ok=True)


def _safe_path(file_path):
    base = os.path.realpath(PROJECT_ROOT)
    path = os.path.realpath(
        file_path if os.path.isabs(file_path)
        else os.path.join(PROJECT_ROOT, file_path)
    )

    if path != base and not path.startswith(base + os.sep):
        raise ValueError("پروجیکٹ فولڈر سے باہر فائل کی اجازت نہیں۔")

    return path


def _load_record():
    if not os.path.exists(BACKUP_RECORD):
        return {"backups": []}

    try:
        with open(BACKUP_RECORD, "r", encoding="utf-8") as f:
            data = json.load(f)

        if not isinstance(data, dict):
            raise ValueError()

        if not isinstance(data.get("backups"), list):
            data["backups"] = []

        return data

    except Exception:
        return {"backups": []}


def _save_record(data):
    try:
        temp = BACKUP_RECORD + ".tmp"

        with open(temp, "w", encoding="utf-8") as f:
            json.dump(
                data,
                f,
                ensure_ascii=False,
                indent=2
            )

        os.replace(temp, BACKUP_RECORD)
        return True

    except Exception:
        return False


def create_backup(file_path, reason=""):
    try:
        full_path = _safe_path(file_path)

        if not os.path.isfile(full_path):
            return False, "فائل موجود نہیں۔"

        timestamp = datetime.now().strftime(
            "%Y%m%d_%H%M%S_%f"
        )

        file_name = os.path.basename(full_path)

        backup_name = (
            f"{file_name}.backup_{timestamp}"
        )

        backup_path = os.path.join(
            BACKUP_FOLDER,
            backup_name
        )

        shutil.copy2(
            full_path,
            backup_path
        )

        record = _load_record()

        record["backups"].append({
            "timestamp": datetime.now().isoformat(),
            "original_file": os.path.relpath(
                full_path,
                PROJECT_ROOT
            ),
            "backup_path": backup_path,
            "reason": reason,
        })

        _save_record(record)
        _cleanup_old_backups()

        return True, backup_path

    except Exception as e:
        return False, f"Backup میں مسئلہ: {e}"


def restore_backup(backup_path):
    try:
        backup_path = _safe_path(backup_path)

        if not os.path.isfile(backup_path):
            return False, "Backup موجود نہیں۔"

        record = _load_record()
        info = None

        for item in record["backups"]:
            if os.path.realpath(
                item.get("backup_path", "")
            ) == backup_path:
                info = item
                break

        if not info:
            return False, "Backup record میں نہیں ملی۔"

        original = info.get("original_file")

        if not original:
            return False, "اصل فائل کا راستہ موجود نہیں۔"

        original_path = _safe_path(original)

        if os.path.exists(original_path):
            ok, result = create_backup(
                original,
                reason="restore سے پہلے safety backup"
            )

            if not ok:
                return False, f"Safety backup ناکام: {result}"

        os.makedirs(
            os.path.dirname(original_path),
            exist_ok=True
        )

        shutil.copy2(
            backup_path,
            original_path
        )

        return True, f"بحال ہو گئی: {original}"

    except Exception as e:
        return False, f"Restore میں مسئلہ: {e}"


def list_backups(file_path=None):
    try:
        record = _load_record()

        if file_path is None:
            return True, record["backups"]

        normalized = os.path.normpath(
            str(file_path)
        )

        results = [
            item
            for item in record["backups"]
            if os.path.normpath(
                str(item.get("original_file", ""))
            ) == normalized
        ]

        return True, results

    except Exception as e:
        return False, f"Backup list میں مسئلہ: {e}"


def _cleanup_old_backups():
    try:
        files = []

        for name in os.listdir(BACKUP_FOLDER):
            path = os.path.join(
                BACKUP_FOLDER,
                name
            )

            if os.path.isfile(path):
                files.append(
                    (path, os.path.getmtime(path))
                )

        files.sort(
            key=lambda item: item[1],
            reverse=True
        )

        for old_file, _ in files[MAX_BACKUPS:]:
            try:
                os.remove(old_file)
            except OSError:
                pass

    except Exception:
        pass
