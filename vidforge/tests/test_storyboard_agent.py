import unittest

from agents.storyboard.agent import StoryboardAgent


class TestStoryboardAgent(unittest.TestCase):

    def setUp(self):
        self.agent = StoryboardAgent()

    def test_single_story_event_creates_one_scene(self):
        scenes = self.agent.run({
            "concept": "Ali walks home.",
            "duration_seconds": 5,
        })

        self.assertEqual(len(scenes), 1)
        self.assertEqual(scenes[0]["description"], "Ali walks home.")

    def test_multiple_events_preserve_story_order(self):
        scenes = self.agent.run({
            "concept": "Ali walks home. Ali opens the door.",
            "duration_seconds": 10,
        })

        self.assertEqual(len(scenes), 2)
        self.assertEqual(
            [scene["description"] for scene in scenes],
            ["Ali walks home.", "Ali opens the door."],
        )

    def test_shot_durations_equal_scene_duration(self):
        scenes = self.agent.run({
            "concept": "Ali walks home.",
            "duration_seconds": 7,
        })

        scene = scenes[0]
        self.assertEqual(
            sum(shot["duration_seconds"] for shot in scene["shots"]),
            scene["duration_seconds"],
        )

    def test_every_shot_has_positive_duration(self):
        scenes = self.agent.run({
            "concept": "Ali walks home.",
            "duration_seconds": 5,
        })

        for scene in scenes:
            for shot in scene["shots"]:
                self.assertGreater(shot["duration_seconds"], 0)

    def test_short_events_are_not_lost_when_events_exceed_seconds(self):
        scenes = self.agent.run({
            "concept": "Ali stands. Ali turns. Ali leaves.",
            "duration_seconds": 5,
        })

        combined_text = " ".join(
            scene["description"] for scene in scenes
        )

        for event in ("Ali stands.", "Ali turns.", "Ali leaves."):
            self.assertIn(event, combined_text)

        self.assertEqual(
            sum(scene["duration_seconds"] for scene in scenes),
            5,
        )


    def test_six_events_with_five_seconds_are_preserved(self):
        scenes = self.agent.run({
            "concept": (
                "Ali stands. Ali turns. Ali walks. "
                "Ali opens the door. Ali enters the room. "
                "Ali sits down."
            ),
            "duration_seconds": 5,
        })

        combined_text = " ".join(
            scene["description"] for scene in scenes
        )

        for event in (
            "Ali stands.",
            "Ali turns.",
            "Ali walks.",
            "Ali opens the door.",
            "Ali enters the room.",
            "Ali sits down.",
        ):
            self.assertIn(event, combined_text)

        self.assertEqual(
            sum(scene["duration_seconds"] for scene in scenes),
            5,
        )



    def test_shots_do_not_use_fixed_framing_template(self):
        scenes = self.agent.run({
            "concept": "Ali opens the door and enters the room.",
            "duration_seconds": 5,
        })

        directions = [
            shot["camera_direction"]
            for scene in scenes
            for shot in scene["shots"]
        ]

        self.assertNotEqual(
            directions[:3],
            ["Wide shot", "Medium shot", "Close-up"],
        )

if __name__ == "__main__":
    unittest.main()
