from typing import Any


class ScriptAgent:
    name = "script_agent"

    def run(self, understood_request: dict[str, Any]) -> dict[str, Any]:
        request = understood_request.get("request", "").strip()
        duration = understood_request.get("duration_seconds", 5)

        if not request:
            raise ValueError("A valid understood request is required")

        return {
            "title": request[:80],
            "duration_seconds": duration,
            "concept": request,
            "narration": None,
            "dialogue": [],
            "scenes_needed": max(1, (duration + 4) // 5),
            "status": "SCRIPT_DRAFT",
        }
