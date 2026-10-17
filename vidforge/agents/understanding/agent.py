from typing import Any


class RequestUnderstandingAgent:
    name = "request_understanding"

    def run(self, user_request: str, duration_seconds: int = 5) -> dict[str, Any]:
        request = user_request.strip()

        if not request:
            raise ValueError("Video request cannot be empty")

        if duration_seconds < 5:
            raise ValueError("Video duration must be at least 5 seconds")

        return {
            "request": request,
            "duration_seconds": duration_seconds,
            "needs_script": True,
            "needs_storyboard": True,
            "needs_character_continuity": True,
            "needs_audio_planning": None,
            "status": "UNDERSTOOD",
        }
