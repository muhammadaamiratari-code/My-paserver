import json
import os
import subprocess
import sys
import urllib.request
from datetime import datetime

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
TEST_FOLDER = os.path.join(
    PROJECT_ROOT,
    "test_results"
)
TEST_RECORD = os.path.join(
    PROJECT_ROOT,
    "test_record.json"
)

os.makedirs(TEST_FOLDER, exist_ok=True)


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
    if not os.path.exists(TEST_RECORD):
        return {"tests": []}

    try:
        with open(TEST_RECORD, "r", encoding="utf-8") as f:
            data = json.load(f)

        if not isinstance(data, dict):
            raise ValueError()

        if not isinstance(data.get("tests"), list):
            data["tests"] = []

        return data

    except Exception:
        return {"tests": []}


def _save_record(data):
    try:
        temp = TEST_RECORD + ".tmp"

        with open(temp, "w", encoding="utf-8") as f:
            json.dump(
                data,
                f,
                ensure_ascii=False,
                indent=2
            )

        os.replace(temp, TEST_RECORD)
        return True

    except Exception:
        return False


def _log_test(test_type, target, success, output):
    record = _load_record()

    record["tests"].append({
        "timestamp": datetime.now().isoformat(),
        "test_type": test_type,
        "target": str(target),
        "success": bool(success),
        "output": str(output)[:4000],
    })

    record["tests"] = record["tests"][-200:]
    _save_record(record)


def run_python_syntax(file_path):
    try:
        full_path = _safe_path(file_path)

        if not os.path.isfile(full_path):
            return False, "فائل موجود نہیں۔"

        result = subprocess.run(
            [
                sys.executable,
                "-m",
                "py_compile",
                full_path
            ],
            capture_output=True,
            text=True,
            timeout=30
        )

        output = (
            result.stdout
            + result.stderr
        ).strip()

        success = result.returncode == 0

        _log_test(
            "python_syntax",
            file_path,
            success,
            output
        )

        return (
            success,
            output if output else "Syntax درست ہے۔"
        )

    except subprocess.TimeoutExpired:
        _log_test(
            "python_syntax",
            file_path,
            False,
            "ٹائم آؤٹ"
        )
        return False, "Syntax test timeout۔"

    except Exception as e:
        return False, f"Syntax test میں مسئلہ: {e}"


def run_python_file(file_path, args=None):
    try:
        full_path = _safe_path(file_path)

        if not os.path.isfile(full_path):
            return False, "فائل موجود نہیں۔"

        cmd = [
            sys.executable,
            full_path
        ]

        if args:
            cmd.extend(
                [str(arg) for arg in args]
            )

        result = subprocess.run(
            cmd,
            capture_output=True,
            text=True,
            timeout=60,
            cwd=PROJECT_ROOT
        )

        output = (
            result.stdout
            + result.stderr
        ).strip()

        success = result.returncode == 0

        _log_test(
            "python_run",
            file_path,
            success,
            output
        )

        return (
            success,
            output if output else "کامیابی سے چل گیا۔"
        )

    except subprocess.TimeoutExpired:
        _log_test(
            "python_run",
            file_path,
            False,
            "ٹائم آؤٹ"
        )
        return False, "Python program timeout۔"

    except Exception as e:
        return False, f"Run میں مسئلہ: {e}"


def check_server_health(
    url="http://127.0.0.1:5000/api/health"
):
    try:
        with urllib.request.urlopen(
            url,
            timeout=10
        ) as response:

            data = response.read().decode(
                "utf-8",
                errors="replace"
            )

            status = response.getcode()
            success = status == 200

            _log_test(
                "server_health",
                url,
                success,
                data
            )

            return success, data

    except Exception as e:
        _log_test(
            "server_health",
            url,
            False,
            str(e)
        )

        return False, f"Server health میں مسئلہ: {e}"


def get_test_history(
    test_type=None,
    target=None
):
    try:
        results = _load_record()["tests"]

        if test_type:
            results = [
                item for item in results
                if item.get("test_type") == test_type
            ]

        if target:
            results = [
                item for item in results
                if item.get("target") == target
            ]

        return True, results

    except Exception as e:
        return False, f"History میں مسئلہ: {e}"
