from pathlib import Path
import subprocess

class TestingAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        # Security check: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        # نئی بہتری: چیک کریں کہ فائل/فولڈر اصل میں موجود ہے یا نہیں
        if not p.exists():
            raise FileNotFoundError(f"Path does not exist: {p}")
        return p

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

    def python_syntax(self, path):
        p = self._path(path)
        if p.suffix != ".py":
            raise ValueError("Python file required")
        return self.run(f'python -m py_compile "{p}"')

    def pytest(self, target=None, timeout=300):
        command = "python -m pytest"
        if target:
            command += f' "{self._path(target)}"'
        return self.run(command, timeout=timeout) # timeout آرگیومنٹ پاس کیا

    def unittest(self, target=None, timeout=300):
        command = "python -m unittest"
        if target:
            command += f' "{self._path(target)}"'
        return self.run(command, timeout=timeout) # timeout آرگیومنٹ پاس کیا

    def command(self, cmd_str, timeout=120):
        return self.run(cmd_str, timeout=timeout)
