from pathlib import Path
import hashlib
import secrets

class SecurityAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        # سیکیورٹی چیک: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        # چیک کریں کہ فائل یا فولڈر اصل میں موجود ہے یا نہیں
        if not p.exists():
            raise FileNotFoundError(f"Path does not exist: {p}")
        return p

    def sha256(self, path):
        h = hashlib.sha256()
        with self._path(path).open("rb") as f:
            for chunk in iter(lambda: f.read(1024 * 1024), b""):
                h.update(chunk)
        return h.hexdigest()

    def secret(self, length=32):
        if length < 16:
            raise ValueError("Minimum secret length is 16")
        return secrets.token_urlsafe(length)

    def sensitive_files(self):
        names = {".env", ".env.local", "credentials.json", "secrets.json"}
        return [
            str(p.relative_to(self.root))
            for p in self.root.glob("**/*")
            if p.is_file() and p.name in names
        ]

    def permission(self, path):
        return oct(self._path(path).stat().st_mode & 0o777)
