from typing import Any


class PromptEngineeringAgent:
    name = "prompt_engineering"

    def run(
        self,
        storyboard: list[dict[str, Any]],
        characters: list[dict[str, Any]] | None = None,
        style: str = "cinematic",
    ) -> list[dict[str, Any]]:
        if not storyboard:
            raise ValueError("Storyboard cannot be empty")

        characters = characters or []
        character_map = {
            character.get("id", f"character_{index + 1}"): character
            for index, character in enumerate(characters)
        }

        prompts = []

        for scene in storyboard:
            description = str(
                scene.get("visual_action") or scene.get("description") or ""
            ).strip()

            if not description:
                raise ValueError("Every scene needs a visual description")

            identities = []
            for character_id in scene.get("character_ids", []):
                character = character_map.get(character_id)
                if character:
                    appearance = str(character.get("appearance", "")).strip()
                    if appearance:
                        identities.append(appearance)

            common_parts = [description]
            if identities:
                common_parts.append(
                    "Character appearance: " + "; ".join(identities)
                )
            common_parts.append("Visual style: " + str(style))

            scene_camera = str(scene.get("camera_direction", "Wide shot"))
            scene_prompt_parts = common_parts + ["Camera: " + scene_camera]

            scene_result = {
                "scene_number": scene.get("scene_number"),
                "duration_seconds": scene.get("duration_seconds", 5),
                "prompt": ". ".join(scene_prompt_parts),
            }

            shot_results = []
            for index, shot in enumerate(scene.get("shots", []), start=1):
                shot_description = str(
                    shot.get("description") or description
                ).strip()
                if not shot_description:
                    raise ValueError(
                        f"Scene {scene.get('scene_number')} has an empty shot"
                    )

                camera = str(shot.get("camera_direction") or scene_camera).strip()
                camera_motion = str(shot.get("camera_motion") or "").strip()

                shot_parts = [shot_description]
                if identities:
                    shot_parts.append(
                        "Character appearance: " + "; ".join(identities)
                    )
                shot_parts.append("Visual style: " + str(style))
                shot_parts.append("Camera: " + camera)
                if camera_motion:
                    shot_parts.append("Camera motion: " + camera_motion)

                shot_results.append({
                    "shot_number": shot.get("shot_number", index),
                    "duration_seconds": shot.get("duration_seconds", 1),
                    "prompt": ". ".join(shot_parts),
                    "camera_direction": camera,
                })

            if shot_results:
                scene_result["shots"] = shot_results

            prompts.append(scene_result)

        return prompts
