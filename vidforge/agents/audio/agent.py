from typing import Any


class AudioPlanningAgent:
    name = "audio_planning"

    def run(
        self,
        script: dict[str, Any],
        audio_requested: bool = True,
    ) -> dict[str, Any]:
        dialogue = script.get("dialogue") or []
        narration = script.get("narration")

        if not audio_requested:
            return {
                "audio_enabled": False,
                "dialogue": [],
                "voiceover": None,
                "music": None,
                "sound_effects": [],
                "status": "AUDIO_DISABLED",
            }

        return {
            "audio_enabled": True,
            "dialogue": dialogue,
            "voiceover": narration,
            "music": "Choose music suitable for the scene",
            "sound_effects": [
                "Use environment-appropriate sounds where relevant"
            ],
            "status": "AUDIO_PLAN_READY",
        }
