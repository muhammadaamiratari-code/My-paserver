from typing import Any


class AIDirector:
    name = "ai_director"

    def validate_request(self, state: dict[str, Any]) -> None:
        request = str(state.get("user_request", "")).strip()
        duration = state.get("duration_seconds", 5)

        if not request:
            raise ValueError("Video request cannot be empty")

        if not isinstance(duration, int) or isinstance(duration, bool):
            raise ValueError("Duration must be a whole number")

        if duration < 5:
            raise ValueError("Video duration must be at least 5 seconds")

    def finalize_planning(self, state: dict[str, Any]) -> dict[str, Any]:
        required = ("understood_request", "script", "storyboard", "prompts")

        missing = [
            field for field in required
            if not state.get(field)
        ]

        if missing:
            return {
                "status": "NEEDS_REVIEW",
                "errors": [
                    *state.get("errors", []),
                    "Planning incomplete: " + ", ".join(missing),
                ],
            }

        return {
            "status": "PLAN_READY",
            "errors": state.get("errors", []),
        }
