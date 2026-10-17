import unittest

from agents.continuity.agent import CharacterContinuityAgent
from agents.prompt.agent import PromptEngineeringAgent


class TestContinuityPromptIntegration(unittest.TestCase):

    def test_each_scene_prompt_uses_only_assigned_character(self):
        characters = [
            {
                "id": "ali",
                "name": "Ali",
                "appearance": "Ali wears a blue shirt",
            },
            {
                "id": "sara",
                "name": "Sara",
                "appearance": "Sara wears a red dress",
            },
        ]

        storyboard = [
            {
                "scene_number": 1,
                "description": "Ali walks home",
                "character_ids": ["ali"],
            },
            {
                "scene_number": 2,
                "description": "Sara opens the door",
                "character_ids": ["sara"],
            },
        ]

        continuity_report = CharacterContinuityAgent().run(
            characters,
            storyboard,
        )

        prompts = PromptEngineeringAgent().run(
            storyboard,
            characters,
        )

        self.assertEqual(continuity_report["scenes_checked"], 2)
        self.assertIn("Ali wears a blue shirt", prompts[0]["prompt"])
        self.assertNotIn("Sara wears a red dress", prompts[0]["prompt"])
        self.assertIn("Sara wears a red dress", prompts[1]["prompt"])
        self.assertNotIn("Ali wears a blue shirt", prompts[1]["prompt"])



    def test_shot_prompt_preserves_camera_direction_motion_and_duration(self):
        storyboard = [{
            "scene_number": 1,
            "description": "Ali opens the door and enters the room",
            "duration_seconds": 5,
            "camera_direction": "Action-focused framing",
            "shots": [{
                "shot_number": 1,
                "description": "Ali opens the door and enters the room",
                "duration_seconds": 5,
                "camera_direction": "Medium shot",
                "camera_motion": "pan left",
            }],
        }]

        prompts = PromptEngineeringAgent().run(storyboard, [])

        shot = prompts[0]["shots"][0]

        self.assertEqual(shot["duration_seconds"], 5)
        self.assertEqual(shot["camera_direction"], "Medium shot")
        self.assertIn("Medium shot", shot["prompt"])
        self.assertIn("pan left", shot["prompt"])

if __name__ == "__main__":
    unittest.main()
