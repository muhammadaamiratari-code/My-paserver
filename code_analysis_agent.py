from pathlib import Path
import ast
import re

class CodeAnalysisAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        # Security check: پروجیکٹ کے باہر کا پاتھ ایکسیس نہ ہو سکے
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        # چیک کریں کہ فائل موجود ہے یا نہیں
        if not p.exists():
            raise FileNotFoundError(f"Path does not exist: {p}")
        return p

    def files(self, pattern="**/*.py"):
        return [str(p.relative_to(self.root)) for p in self.root.glob(pattern) if p.is_file()]

    def read(self, path):
        return self._path(path).read_text(encoding="utf-8")

    def syntax(self, path):
        try:
            ast.parse(self.read(path), filename=str(path))
            return {"ok": True, "error": None}
        except SyntaxError as e:
            return {"ok": False, "error": f"{e.msg} at line {e.lineno}"}
        except Exception as e:
            return {"ok": False, "error": str(e)}

    def imports(self, path):
        try:
            tree = ast.parse(self.read(path), filename=str(path))
            return sorted({n.names[0].name for n in ast.walk(tree) if isinstance(n, ast.Import)}
                          | {n.module for n in ast.walk(tree) if isinstance(n, ast.ImportFrom) and n.module})
        except SyntaxError:
            return [] # اگر سینٹیکس ایرر ہو تو پروگرام کریش ہونے کے بجائے خالی لسٹ واپس کرے

    def symbols(self, path):
        try:
            tree = ast.parse(self.read(path), filename=str(path))
            return {
                "classes": [n.name for n in ast.walk(tree) if isinstance(n, ast.ClassDef)],
                "functions": [n.name for n in ast.walk(tree) if isinstance(n, (ast.FunctionDef, ast.AsyncFunctionDef))]
            }
        except SyntaxError:
            return {"classes": [], "functions": []} # ایرر کی صورت میں خالی ڈیٹا

    def search(self, text, pattern):
        results = []
        for p in self.root.glob("**/*"):
            if p.is_file():
                try:
                    # بگ فکس: فائل کو دو بار پڑھنے کے بجائے ایک بار ویری ایبل میں سٹور کریں
                    content = p.read_text(encoding="utf-8", errors="ignore")
                    if text in content and re.search(pattern, content):
                        results.append(str(p.relative_to(self.root)))
                except Exception:
                    continue
        return results

    def analyze(self, path):
        syn = self.syntax(path)
        # لاجک فکس: اگر سینٹیکس ٹھیک نہیں ہے تو امپورٹس اور سمبلز چیک کرنے کی ضرورت نہیں
        if not syn.get("ok"):
            return {
                "path": path,
                "syntax": syn,
                "imports": [],
                "symbols": {"classes": [], "functions": []}
            }
        
        return {
            "path": path,
            "syntax": syn,
            "imports": self.imports(path),
            "symbols": self.symbols(path)
        }
