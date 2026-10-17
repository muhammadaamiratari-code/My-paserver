"""Registry for selectable VidForge video engines."""

import os
from router.providers.comfyui import ComfyUIEngine


def get_engine(name: str = "comfyui"):
    if name.lower() == "comfyui":
        url = os.getenv(
            "VIDFORGE_COMFYUI_URL",
            "http://127.0.0.1:8188"
        )
        return ComfyUIEngine(url)

    raise ValueError(f"Unsupported video engine: {name}")
