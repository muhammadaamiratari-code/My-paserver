import unittest

from agents.continuity.agent import CharacterContinuityAgent


class TestCharacterContinuityAgent(unittest.TestCase):

    def setUp(self):
        self.agent = CharacterContinuityAgent()
        self.characters = [
            {"id": "ali", "name": "Ali", "appearance": "Blue shirt"},
            {"id": "sara", "name": "Sara", "appearance": "Red dress"},
        ]

    def test_preserves_explicit_scene_character_assignments(self):
        scenes = [
            {"scene_number": 1, "character_ids": ["ali"]},
            {"scene_number": 2, "character_ids": []},
        ]

        self.agent.run(self.characters, scenes)

        self.assertEqual(scenes[0]["character_ids"], ["ali"])
        self.assertEqual(scenes[1]["character_ids"], [])

    def test_does_not_assume_all_characters_are_in_every_scene(self):
        scenes = [
            {"scene_number": 1, "description": "A door opens"},
        ]

        self.agent.run(self.characters, scenes)

        self.assertEqual(scenes[0]["character_ids"], [])

    def test_detects_only_character_named_in_scene(self):
        scenes = [
            {"scene_number": 1, "description": "Ali walks home."},
        ]

        self.agent.run(self.characters, scenes)

        self.assertEqual(scenes[0]["character_ids"], ["ali"])

    def test_rejects_unknown_character_id(self):
        scenes = [
            {"scene_number": 1, "character_ids": ["unknown"]},
        ]

        with self.assertRaisesRegex(ValueError, "Unknown character IDs"):
            self.agent.run(self.characters, scenes)

    def test_rejects_duplicate_character_ids_in_scene(self):
        scenes = [
            {"scene_number": 1, "character_ids": ["ali", "ali"]},
        ]

        with self.assertRaisesRegex(ValueError, "Duplicate character ID"):
            self.agent.run(self.characters, scenes)

    def test_rejects_duplicate_registered_character_ids(self):
        characters = [
            {"id": "ali", "appearance": "Blue shirt"},
            {"id": "ali", "appearance": "Green shirt"},
        ]

        with self.assertRaisesRegex(ValueError, "Duplicate character ID"):
            self.agent.run(
                characters,
                [{"scene_number": 1, "character_ids": []}],
            )

    def test_accepts_storyboard_with_no_registered_characters(self):
        scenes = [
            {"scene_number": 1, "description": "An empty room"},
        ]

        report = self.agent.run([], scenes)

        self.assertEqual(scenes[0]["character_ids"], [])
        self.assertEqual(report["characters_registered"], 0)
        self.assertEqual(report["scenes_checked"], 1)
        self.assertEqual(report["status"], "CONTINUITY_READY")


if __name__ == "__main__":
    unittest.main()
