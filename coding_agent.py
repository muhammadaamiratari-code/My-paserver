from pathlib import Path
import shutil
import subprocess
from datetime import datetime


class CodingAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()
        self.backup_dir = self.root / ".coding_agent_backups"

    def _path(self, path):
        p = (self.root / path).resolve()
        # سیکیورٹی چیک: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        return p

    def list_files(self, pattern="**/*"):
        return [
            str(p.relative_to(self.root))
            for p in self.root.glob(pattern)
            if p.is_file() and ".coding_agent_backups" not in p.parts
        ]

    def read(self, path, encoding="utf-8"):
        return self._path(path).read_text(encoding=encoding)

    def write(self, path, content, backup=True, encoding="utf-8"):
        p = self._path(path)
        p.parent.mkdir(parents=True, exist_ok=True)
        if backup and p.exists():
            self.backup(path)
        p.write_text(content, encoding=encoding)
        return str(p.relative_to(self.root))

    def patch(self, path, old, new, backup=True, encoding="utf-8"):
        text = self.read(path, encoding)
        count = text.count(old)
        if count != 1:
            raise ValueError(f"Expected 1 match, found {count}")
        return self.write(path, text.replace(old, new, 1), backup, encoding)

    def backup(self, path):
        src = self._path(path)
        if not src.is_file():
            raise FileNotFoundError(path)
        stamp = datetime.now().strftime("%Y%m%d-%H%M%S-%f")
        dest = self.backup_dir / stamp / src.relative_to(self.root)
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(src, dest)
        return str(dest.relative_to(self.root))

    def run(self, command, timeout=60):
        try:
            result = subprocess.run(
                command,
                cwd=self.root,
                shell=True,
                capture_output=True,
                text=True,
                timeout=timeout
            )
            return {
                "returncode": result.returncode,
                "stdout": result.stdout,
                "stderr": result.stderr,
                "timeout": False # یہ بتانے کے لیے کہ ٹائم آؤٹ نہیں ہوا
            }
        # بگ فکس: اگر کمانڈ اٹک جائے اور ٹائم آؤٹ ہو جائے
        except subprocess.TimeoutExpired as e:
            return {
                "returncode": -1,
                "stdout": e.stdout or "",
                "stderr": e.stderr or f"Error: Command timed out after {timeout} seconds.",
                "timeout": True
            }

    def test_python(self, path):
        p = self._path(path)
        if p.suffix != ".py":
            raise ValueError("Python file required")
        return self.run(f'python "{p}"')

    def syntax_check(self, path):
        p = self._path(path)
        if p.suffix != ".py":
            raise ValueError("Python file required")
        return self.run(f'python -m py_compile "{p}"')

    def info(self):
        return {
            "root": str(self.root),
            "files": len(self.list_files()),
            "backup_dir": str(self.backup_dir)
        }
