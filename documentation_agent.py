from pathlib import Path

class DocumentationAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()

    def _path(self, path):
        p = (self.root / path).resolve()
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        return p

    def read(self, path):
        return self._path(path).read_text(encoding="utf-8")

    def write(self, path, content):
        p = self._path(path)
        p.parent.mkdir(parents=True, exist_ok=True)
        p.write_text(content, encoding="utf-8")
        return str(p.relative_to(self.root))

    def append(self, path, content):
        p = self._path(path)
        p.parent.mkdir(parents=True, exist_ok=True)
        with p.open("a", encoding="utf-8") as f:
            f.write(content)
        return str(p.relative_to(self.root))

    def files(self, pattern="**/*.md"):
        return [str(p.relative_to(self.root)) for p in self.root.glob(pattern) if p.is_file()]
