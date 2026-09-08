from pathlib import Path
import subprocess

class DeploymentAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        # سیکیورٹی چیک: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        # چیک کریں کہ فائل اصل میں موجود ہے یا نہیں
        if not p.exists():
            raise FileNotFoundError(f"Path does not exist: {p}")
        return p

    def run(self, command, timeout=300):
        try:
            r = subprocess.run(command, cwd=self.root, shell=True,
                               capture_output=True, text=True, timeout=timeout)
            return {
                "passed": r.returncode == 0,
                "returncode": r.returncode,
                "stdout": r.stdout,
                "stderr": r.stderr,
                "timeout": False # یہ بتانے کے لیے کہ ٹائم آؤٹ نہیں ہوا
            }
        # بگ فکس: اگر کمانڈ یا ڈپائلمنٹ پراسیس اٹک جائے اور ٹائم آؤٹ ہو جائے
        except subprocess.TimeoutExpired as e:
            return {
                "passed": False,
                "returncode": -1,
                "stdout": e.stdout or "",
                "stderr": e.stderr or f"Error: Command timed out after {timeout} seconds.",
                "timeout": True
            }

    def git_status(self):
        return self.run("git status --short")

    def git_branch(self):
        return self.run("git branch --show-current")

    def syntax_check(self, path):
        # سیکیورٹی اور فائل وجود کی جانچ کے لیے _path کا استعمال
        p = self._path(path)
        if p.suffix != ".py":
            raise ValueError("Python file required")
        return self.run(f'python -m py_compile "{p}"')

    def health(self, command):
        return self.run(command, 60)
