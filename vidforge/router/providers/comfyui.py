"""ComfyUI adapter for VidForge."""

import json
from urllib.request import Request, urlopen
from router.providers.base import VideoEngine


class ComfyUIEngine(VideoEngine):
    def __init__(self, base_url: str):
        self.base_url = base_url.rstrip("/")

    def submit_job(self, request: dict) -> dict:
        payload = json.dumps({
            "prompt": request["workflow"]
        }).encode("utf-8")
        req = Request(
            f"{self.base_url}/prompt",
            data=payload,
            headers={"Content-Type": "application/json"},
            method="POST",
        )
        with urlopen(req, timeout=30) as response:
            result = json.loads(response.read().decode("utf-8"))
        return {
            "engine": "comfyui",
            "job_id": result["prompt_id"],
            "status": "SUBMITTED",
        }

    def get_job_status(self, job_id: str) -> dict:
        with urlopen(
            f"{self.base_url}/history/{job_id}", timeout=30
        ) as response:
            result = json.loads(response.read().decode("utf-8"))
        job = result.get(job_id)
        return {
            "engine": "comfyui",
            "job_id": job_id,
            "status": "COMPLETED" if job else "RUNNING",
            "result": job,
        }
