import json
import os
from datetime import datetime

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
BACKUP_FOLDER = os.path.join(PROJECT_ROOT, "dev_backups")
MEMORY_FILE = os.path.join(PROJECT_ROOT, "dev_tools_memory.json")

SKIP_FOLDERS = {
    "dev_backups", "__pycache__", ".git",
    "node_modules", ".venv", "venv",
}

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


def _load_memory():
    if not os.path.exists(MEMORY_FILE):
        return {"changes": [], "bugs": [], "edits": []}

    try:
        with open(MEMORY_FILE, "r", encoding="utf-8") as f:
            data = json.load(f)

        if not isinstance(data, dict):
            raise ValueError("Memory format invalid.")

        for key in ("changes", "bugs", "edits"):
            if not isinstance(data.get(key), list):
                data[key] = []

        return data

    except Exception:
        return {"changes": [], "bugs": [], "edits": []}


def _save_memory(data):
    try:
        temp = MEMORY_FILE + ".tmp"

        with open(temp, "w", encoding="utf-8") as f:
            json.dump(
                data,
                f,
                ensure_ascii=False,
                indent=2
            )

        os.replace(temp, MEMORY_FILE)
        return True

    except Exception:
        return False


def _log_change(action, file_path, detail=""):
    data = _load_memory()

    data["changes"].append({
        "timestamp": datetime.now().isoformat(),
        "action": action,
        "file": str(file_path),
        "detail": str(detail),
    })

    data["changes"] = data["changes"][-200:]
    _save_memory(data)


def read_file(file_path):
    try:
        full_path = _safe_path(file_path)

        if not os.path.isfile(full_path):
            return False, "فائل موجود نہیں۔"

        with open(full_path, "r", encoding="utf-8") as f:
            return True, f.read()

    except Exception as e:
        return False, f"فائل پڑھنے میں مسئلہ: {e}"


def find_errors(file_path):
    success, content = read_file(file_path)

    if not success:
        return False, content

    issues = []

    if content.count("(") != content.count(")"):
        issues.append("قوسین کا توازن درست نہیں۔")

    if content.count("{") != content.count("}"):
        issues.append("Curly brackets کا توازن درست نہیں۔")

    if content.count("[") != content.count("]"):
        issues.append("Square brackets کا توازن درست نہیں۔")

    long_lines = [
        i + 1
        for i, line in enumerate(content.splitlines())
        if len(line) > 120
    ]

    if long_lines:
        issues.append(
            f"120 characters سے لمبی لائنیں: {long_lines}"
        )

    todo_lines = [
        i + 1
        for i, line in enumerate(content.splitlines())
        if "TODO" in line or "FIXME" in line
    ]

    if todo_lines:
        issues.append(
            f"TODO/FIXME لائنیں: {todo_lines}"
        )

    if not issues:
        return True, "کوئی واضح سطحی مسئلہ نہیں ملا۔"

    return True, issues


def edit_file(
    file_path,
    old_text,
    new_text,
    create_backup=True
):
    try:
        full_path = _safe_path(file_path)

        if not os.path.isfile(full_path):
            return False, "فائل موجود نہیں۔"

        if not old_text:
            return False, "پرانا متن خالی نہیں ہو سکتا۔"

        with open(full_path, "r", encoding="utf-8") as f:
            content = f.read()

        if old_text not in content:
            return False, "پرانا متن فائل میں نہیں ملا۔"

        if create_backup:
            from backup_manager import create_backup as make_backup
            ok, result = make_backup(
                file_path,
                reason="قبل از edit"
            )

            if not ok:
                return False, f"Backup ناکام: {result}"

        updated = content.replace(
            old_text,
            new_text,
            1
        )

        temp_path = full_path + ".tmp"

        with open(temp_path, "w", encoding="utf-8") as f:
            f.write(updated)

        os.replace(temp_path, full_path)

        _log_change(
            "edit",
            file_path,
            "تبدیلی کامیابی سے محفوظ ہوئی۔"
        )

        return True, "تبدیلی کامیاب رہی۔"

    except Exception as e:
        return False, f"تبدیلی میں مسئلہ: {e}"


def add_code(
    file_path,
    new_code,
    position="end",
    create_backup=True
):
    try:
        full_path = _safe_path(file_path)

        if not os.path.isfile(full_path):
            return False, "فائل موجود نہیں۔"

        if not new_code:
            return False, "نیا code خالی نہیں ہو سکتا۔"

        if create_backup:
            from backup_manager import create_backup as make_backup
            ok, result = make_backup(
                file_path,
                reason="قبل از add_code"
            )

            if not ok:
                return False, f"Backup ناکام: {result}"

        with open(full_path, "r", encoding="utf-8") as f:
            content = f.read()

        if position == "end":
            updated = (
                content.rstrip()
                + "\n\n"
                + new_code
                + "\n"
            )

        elif position == "start":
            updated = new_code + "\n\n" + content

        else:
            return False, "position صرف start یا end ہو سکتی ہے۔"

        temp_path = full_path + ".tmp"

        with open(temp_path, "w", encoding="utf-8") as f:
            f.write(updated)

        os.replace(temp_path, full_path)

        _log_change(
            "add",
            file_path,
            f"نیا code position={position}"
        )

        return True, "نیا code شامل ہو گیا۔"

    except Exception as e:
        return False, f"code شامل کرنے میں مسئلہ: {e}"


def list_project_files(extensions=None):
    try:
        extensions = tuple(extensions) if extensions else None
        files_list = []

        for root, dirs, files in os.walk(PROJECT_ROOT):
            dirs[:] = [
                d for d in dirs
                if d not in SKIP_FOLDERS
            ]

            for name in files:
                if extensions and not name.endswith(extensions):
                    continue

                path = os.path.join(root, name)
                files_list.append(
                    os.path.relpath(path, PROJECT_ROOT)
                )

        return True, sorted(files_list)

    except Exception as e:
        return False, f"فائلز دیکھنے میں مسئلہ: {e}"
