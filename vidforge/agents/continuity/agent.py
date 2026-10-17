from typing import Any
import re


class CharacterContinuityAgent:
    name = "character_continuity"

    @staticmethod
    def _scene_text(scene: dict[str, Any]) -> str:
        parts = []
        for key in ("description", "visual_action", "action", "text", "beat"):
            value = scene.get(key)
            if isinstance(value, str) and value.strip():
                parts.append(value.strip())
        return " ".join(parts)

    @staticmethod
    def _mentions_character(
        text: str,
        character: dict[str, Any],
        character_id: str,
    ) -> bool:
        candidates = []

        name = character.get("name")
        if isinstance(name, str) and name.strip():
            candidates.append(name.strip())

        aliases = character.get("aliases", [])
        if isinstance(aliases, list):
            candidates.extend(
                alias.strip()
                for alias in aliases
                if isinstance(alias, str) and alias.strip()
            )

        if not candidates and not re.fullmatch(
            r"character[_ -]?\d+",
            character_id,
            flags=re.IGNORECASE,
        ):
            candidates.append(character_id.replace("_", " ").replace("-", " "))

        for candidate in candidates:
            pattern = r"(?<!\w)" + re.escape(candidate) + r"(?!\w)"
            if re.search(pattern, text, flags=re.IGNORECASE):
                return True

        return False

    def run(
        self,
        characters: list[dict[str, Any]],
        storyboard: list[dict[str, Any]],
    ) -> dict[str, Any]:
        character_map = {}
        checked_scenes = 0
        inferred_assignments = 0
        unassigned_scenes = 0

        for index, character in enumerate(characters):
            character_id = character.get("id", f"character_{index + 1}")

            if not isinstance(character_id, str) or not character_id.strip():
                raise ValueError("Every character needs a valid string ID")

            if character_id in character_map:
                raise ValueError(f"Duplicate character ID: {character_id}")

            if not character.get("appearance"):
                raise ValueError(
                    f"Character {character_id} needs an appearance description"
                )

            character_map[character_id] = character

        for scene in storyboard:
            if not isinstance(scene, dict):
                raise ValueError("Every storyboard scene must be an object")

            if "character_ids" in scene:
                assigned = scene["character_ids"]

                if not isinstance(assigned, list):
                    raise ValueError("Scene character_ids must be a list")

                if len(assigned) != len(set(assigned)):
                    raise ValueError("Duplicate character ID in a scene")

                unknown_ids = [
                    character_id
                    for character_id in assigned
                    if character_id not in character_map
                ]
                if unknown_ids:
                    raise ValueError(
                        "Unknown character IDs in scene: "
                        + ", ".join(map(str, unknown_ids))
                    )

                scene["character_ids"] = list(assigned)
            else:
                text = self._scene_text(scene)
                assigned = [
                    character_id
                    for character_id, character in character_map.items()
                    if self._mentions_character(
                        text,
                        character,
                        character_id,
                    )
                ]
                scene["character_ids"] = assigned
                inferred_assignments += len(assigned)

            if not scene["character_ids"]:
                unassigned_scenes += 1

            checked_scenes += 1

        return {
            "characters_registered": len(character_map),
            "scenes_checked": checked_scenes,
            "character_ids": sorted(character_map),
            "inferred_assignments": inferred_assignments,
            "scenes_without_confirmed_characters": unassigned_scenes,
            "status": "CONTINUITY_READY",
        }
