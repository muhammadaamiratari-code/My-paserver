from pathlib import Path
import ast
import shutil
from datetime import datetime

class DebugFixAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()
        self.backups = self.root / ".debug_fix_backups"

    def _path(self, path):
        p = (self.root / path).resolve()
        # سیکیورٹی چیک: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        # چیک کریں کہ فائل موجود ہے یا نہیں
        if not p.exists():
            raise FileNotFoundError(f"Path does not exist: {p}")
        return p

    def read(self, path):
        return self._path(path).read_text(encoding="utf-8")

    def backup(self, path):
        src = self._path(path)
        if not src.is_file():
            raise FileNotFoundError(path)
        dest = self.backups / datetime.now().strftime("%Y%m%d-%H%M%S-%f") / src.relative_to(self.root)
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(src, dest)
        return str(dest.relative_to(self.root))

    def syntax(self, path):
        try:
            ast.parse(self.read(path), filename=str(path))
            return {"ok": True}
        except SyntaxError as e:
            return {"ok": False, "line": e.lineno, "message": e.msg}
        except Exception as e:
            # بگ فکس: اگر فائل نہ ملے یا کوئی اور ایرر ہو تو پروگرام کریش ہونے کے بجائے ڈکشنری واپس کرے
            return {"ok": False, "line": None, "message": str(e)}

    def replace(self, path, old, new):
        p = self._path(path)
        text = p.read_text(encoding="utf-8")
        count = text.count(old)
        if count != 1:
            raise ValueError(f"Expected 1 match, found {count}")
        self.backup(path)
        p.write_text(text.replace(old, new, 1), encoding="utf-8")
        return self.syntax(path)
