from pathlib import Path
import shutil
from datetime import datetime

class BackupRecoveryAgent:
    def __init__(self, root="."):
        self.root = Path(root).resolve()
        self.backups = self.root / ".project_backups"

    def _path(self, path):
        p = (self.root / path).resolve()
        if p != self.root and self.root not in p.parents:
            raise ValueError("Path outside project is not allowed")
        return p

    def backup(self, path):
        src = self._path(path)
        if not src.is_file():
            raise FileNotFoundError(path)
        dest = self.backups / datetime.now().strftime("%Y%m%d-%H%M%S-%f") / src.relative_to(self.root)
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copy2(src, dest)
        return str(dest.relative_to(self.root))

    def list_backups(self):
        if not self.backups.exists():
            return []
        return [str(p.relative_to(self.root)) for p in self.backups.rglob("*") if p.is_file()]

    def restore(self, backup_path, target_path=None):
        src = self._path(backup_path)
        if not src.is_file() or self.backups not in src.parents:
            raise ValueError("Invalid backup path")
        target = self._path(target_path or str(src.relative_to(self.backups)).split("/", 1)[-1])
        target.parent.mkdir(parents=True, exist_ok=True)
        if target.exists():
            raise FileExistsError(str(target))
        shutil.copy2(src, target)
        return str(target.relative_to(self.root))
