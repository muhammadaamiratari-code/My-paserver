"""VidForge basic quality-control worker."""

from pathlib import Path
import subprocess


def check_video(path):
    video = Path(path)

    if not video.is_file():
        return {"ok": False, "error": "file_not_found"}

    result = subprocess.run(
        [
            "ffprobe",
            "-v", "error",
            "-select_streams", "v:0",
            "-show_entries", "stream=codec_name,width,height,duration",
            "-of", "json",
            str(video),
        ],
        capture_output=True,
        text=True,
    )

    if result.returncode != 0:
        return {"ok": False, "error": "ffprobe_failed"}

    return {"ok": True, "video": result.stdout}
