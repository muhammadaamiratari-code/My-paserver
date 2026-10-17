from typing import Any
import re


class StoryboardAgent:
    name = "storyboard_agent"

    @staticmethod
    def _split_story_units(text: str) -> list[str]:
        text = text.strip()
        if not text:
            return []

        units = []

        for paragraph in re.split(r"[\r\n]+", text):
            paragraph = paragraph.strip()
            if not paragraph:
                continue

            sentences = re.split(
                r"(?<=[.!?۔؟])\s+",
                paragraph,
            )

            units.extend(
                sentence.strip()
                for sentence in sentences
                if sentence.strip()
            )

        return units

    @staticmethod
    def _scene_text(item: Any) -> str:
        if isinstance(item, str):
            return item.strip()

        if isinstance(item, dict):
            for key in ("description", "visual_action", "action", "text", "beat"):
                value = item.get(key)
                if isinstance(value, str) and value.strip():
                    return value.strip()

        return ""

    @staticmethod
    def _divide_duration(total: int, count: int) -> list[int]:
        if count < 1 or total < count:
            raise ValueError(
                "Not enough whole seconds to assign a positive duration "
                "to every scene or shot"
            )

        base, remainder = divmod(total, count)
        return [
            base + (1 if index < remainder else 0)
            for index in range(count)
        ]

    def run(self, script: dict[str, Any]) -> list[dict[str, Any]]:
        duration = script.get("duration_seconds", 5)

        if (
            not isinstance(duration, int)
            or isinstance(duration, bool)
            or duration < 1
        ):
            raise ValueError("Duration must be a positive whole number")

        concept = str(script.get("concept", "")).strip()
        supplied_scenes = script.get("scenes")

        if isinstance(supplied_scenes, list) and supplied_scenes:
            story_units = [
                self._scene_text(item)
                for item in supplied_scenes
            ]
            story_units = [unit for unit in story_units if unit]
        else:
            if not concept:
                raise ValueError("A valid script concept is required")
            story_units = self._split_story_units(concept)

        if not story_units:
            raise ValueError("No usable story scenes or events were found")

        if len(story_units) > duration:
            group_count = duration
            base, remainder = divmod(len(story_units), group_count)
            grouped_units = []
            position = 0

            for index in range(group_count):
                size = base + (1 if index < remainder else 0)
                grouped_units.append(
                    " ".join(story_units[position:position + size])
                )
                position += size

            story_units = grouped_units

        scene_durations = self._divide_duration(
            duration,
            len(story_units),
        )

        scenes = []

        for index, (story_text, scene_duration) in enumerate(
            zip(story_units, scene_durations),
            start=1,
        ):
            shots = [{
                "shot_number": 1,
                "description": (
                    "Show the stated story action in its original order: "
                    f"{story_text}"
                ),
                "duration_seconds": scene_duration,
                "camera_direction": "Action-focused framing",
                "camera_motion": "static",
            }]

            scenes.append({
                "scene_number": index,
                "duration_seconds": scene_duration,
                "description": story_text,
                "visual_action": story_text,
                "camera_direction": "Action-focused framing",
                "camera_motion": "static",
                "transition": "cut",
                "shots": shots,
            })

        return scenes
