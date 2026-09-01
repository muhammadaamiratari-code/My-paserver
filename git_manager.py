import json
import os
import re
import subprocess
from datetime import datetime

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
GIT_RECORD = os.path.join(PROJECT_ROOT, "git_manager_record.json")
_BRANCH_RE = re.compile(r"^[A-Za-z0-9._/-]+$")


def _load_record():
    if not os.path.exists(GIT_RECORD):
        return {"operations": []}
    try:
        with open(GIT_RECORD, "r", encoding="utf-8") as f:
            data = json.load(f)
        if not isinstance(data, dict):
            raise ValueError()
        if not isinstance(data.get("operations"), list):
            data["operations"] = []
        return data
    except Exception:
        return {"operations": []}


def _save_record(data):
    try:
        temp = GIT_RECORD + ".tmp"
        with open(temp, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        os.replace(temp, GIT_RECORD)
        return True
    except Exception:
        return False


def _repo(repo_path=None):
    return os.path.realpath(repo_path if repo_path else PROJECT_ROOT)


def _safe_repo_file(file_path, repo_path):
    if not file_path:
        return None
    repo = _repo(repo_path)
    path = os.path.realpath(
        file_path if os.path.isabs(file_path) else os.path.join(repo, file_path)
    )
    if path != repo and not path.startswith(repo + os.sep):
        raise ValueError("فائل repository سے باہر نہیں ہو سکتی۔")
    return path


def _valid_branch(branch):
    branch = str(branch).strip()
    return bool(branch) and not branch.startswith("-") and bool(_BRANCH_RE.fullmatch(branch))


def _run_git(operation, args, repo_path=None, timeout=15):
    cwd = _repo(repo_path)
    if not os.path.isdir(os.path.join(cwd, ".git")):
        return False, "یہ Git repository نہیں ہے۔"
    try:
        result = subprocess.run(
            ["git"] + args,
            capture_output=True,
            text=True,
            cwd=cwd,
            timeout=timeout
        )
        output = (result.stdout + result.stderr).strip()
        success = result.returncode == 0
        _log_git(operation, cwd, success, output)
        return success, output
    except subprocess.TimeoutExpired:
        _log_git(operation, cwd, False, "Git command timeout")
        return False, "Git command timeout۔"
    except Exception as e:
        return False, f"Git میں مسئلہ: {e}"


def git_status(repo_path=None):
    return _run_git("status", ["status", "--short", "--branch"], repo_path)


def git_diff(file_path=None, repo_path=None):
    args = ["diff"]
    if file_path:
        try:
            _safe_repo_file(file_path, repo_path)
        except ValueError as e:
            return False, str(e)
        args.extend(["--", file_path])
    return _run_git("diff", args, repo_path)


def git_add(file_path, repo_path=None):
    if not file_path:
        return False, "فائل کا نام ضروری ہے۔"
    try:
        _safe_repo_file(file_path, repo_path)
    except ValueError as e:
        return False, str(e)
    return _run_git("add", ["add", "--", file_path], repo_path)


def git_commit(message, repo_path=None):
    if not message or not message.strip():
        return False, "Commit message ضروری ہے۔"
    return _run_git("commit", ["commit", "-m", message.strip()], repo_path)


def git_push(branch="main", repo_path=None):
    if not _valid_branch(branch):
        return False, "Branch name درست نہیں ہے۔"
    return _run_git("push", ["push", "origin", branch.strip()], repo_path, timeout=60)


def git_pull(branch="main", repo_path=None):
    if not _valid_branch(branch):
        return False, "Branch name درست نہیں ہے۔"
    return _run_git("pull", ["pull", "--ff-only", "origin", branch.strip()], repo_path, timeout=60)


def git_log(limit=10, repo_path=None):
    try:
        limit = max(1, min(int(limit), 100))
    except (TypeError, ValueError):
        return False, "limit درست number ہونا چاہیے۔"
    return _run_git("log", ["log", f"--max-count={limit}", "--oneline"], repo_path)


def git_create_branch(branch_name, repo_path=None):
    if not _valid_branch(branch_name):
        return False, "Branch name درست نہیں ہے۔"
    return _run_git("create_branch", ["checkout", "-b", branch_name.strip()], repo_path)


def _log_git(operation, repo_path, success, output):
    record = _load_record()
    record["operations"].append({
        "timestamp": datetime.now().isoformat(),
        "operation": operation,
        "repo_path": repo_path,
        "success": bool(success),
        "output": str(output)[:4000],
    })
    record["operations"] = record["operations"][-200:]
    _save_record(record)
