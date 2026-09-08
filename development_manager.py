from pathlib import Path
import subprocess

class DevelopmentManager:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        # سیکیورٹی چیک: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        # چیک کریں کہ فائل موجود ہے یا نہیں
        if not p.exists():
            raise FileNotFoundError(f"Path does not exist: {p}")
        return p

    def status(self):
        r = subprocess.run("git status --short", cwd=self.root,
                           shell=True, capture_output=True, text=True)
        return {"returncode": r.returncode, "output": r.stdout, "error": r.stderr}

    def files(self, pattern="**/*"):
        return [str(p.relative_to(self.root)) for p in self.root.glob(pattern)
                if p.is_file() and ".git" not in p.parts]

    def read(self, path):
        return self._path(path).read_text(encoding="utf-8")

    def run(self, command, timeout=120):
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
        # بگ فکس: اگر کمانڈ اٹک جائے اور ٹائم آؤٹ ہو جائے
        except subprocess.TimeoutExpired as e:
            return {
                "passed": False,
                "returncode": -1,
                "stdout": e.stdout or "",
                "stderr": e.stderr or f"Error: Command timed out after {timeout} seconds.",
                "timeout": True
            }

    def task(self, command, timeout=120):
        return self.run(command, timeout)
