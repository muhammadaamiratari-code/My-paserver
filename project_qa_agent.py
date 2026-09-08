from pathlib import Path
import ast

class ProjectQAAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        # سیکیورٹی چیک: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        return p

    def files(self, pattern="**/*.py"):
        return [str(p.relative_to(self.root)) for p in self.root.glob(pattern) if p.is_file()]

    def check_python(self, path):
        try:
            p = self._path(path)
            # چیک کریں کہ آیا یہ واقعی فائل ہے یا نہیں
            if not p.is_file():
                return {"passed": False, "file": path, "line": None, "error": "Not a valid file"}
                
            ast.parse(p.read_text(encoding="utf-8"), filename=str(path))
            return {"passed": True, "file": path}
        except SyntaxError as e:
            return {
                "passed": False,
                "file": path,
                "line": e.lineno,
                "error": e.msg
            }
        except Exception as e:
            # بگ فکس: کسی بھی دوسرے ایرر (جیسے فائل نہ ملنے) پر پروگرام کریش نہیں ہوگا
            return {
                "passed": False,
                "file": path,
                "line": None,
                "error": str(e)
            }

    def project_check(self):
        results = [self.check_python(f) for f in self.files()]
        return {"passed": all(x["passed"] for x in results), "results": results}

    def exists(self, path):
        return self._path(path).exists()
