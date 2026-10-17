from langgraph.graph import StateGraph, START, END

from agents.state import VidForgeState
from agents.director import AIDirector
from agents.understanding.agent import RequestUnderstandingAgent
from agents.script.agent import ScriptAgent
from agents.storyboard.agent import StoryboardAgent
from agents.continuity.agent import CharacterContinuityAgent
from agents.prompt.agent import PromptEngineeringAgent
from agents.audio.agent import AudioPlanningAgent
from agents.quality_review.agent import AIQualityReviewAgent


director = AIDirector()
understanding_agent = RequestUnderstandingAgent()
script_agent = ScriptAgent()
storyboard_agent = StoryboardAgent()
continuity_agent = CharacterContinuityAgent()
prompt_agent = PromptEngineeringAgent()
audio_agent = AudioPlanningAgent()
quality_agent = AIQualityReviewAgent()


def understand(state: VidForgeState):
    director.validate_request(state)
    result = understanding_agent.run(
        state["user_request"],
        state.get("duration_seconds", 5),
    )
    return {"understood_request": result}


def write_script(state: VidForgeState):
    return {
        "script": script_agent.run(state["understood_request"])
    }


def create_storyboard(state: VidForgeState):
    return {
        "storyboard": storyboard_agent.run(state["script"])
    }


def check_continuity(state: VidForgeState):
    characters = state.get("characters", [])
    storyboard = [dict(scene) for scene in state["storyboard"]]

    report = continuity_agent.run(characters, storyboard)

    return {
        "storyboard": storyboard,
        "continuity_report": report,
    }


def create_prompts(state: VidForgeState):
    return {
        "prompts": prompt_agent.run(
            state["storyboard"],
            state.get("characters", []),
            state.get("style", "cinematic"),
        )
    }


def plan_audio(state: VidForgeState):
    return {
        "audio_plan": audio_agent.run(
            state["script"],
            state.get("audio_requested", True),
        )
    }


def finish_planning(state: VidForgeState):
    return director.finalize_planning(state)


def build_workflow():
    graph = StateGraph(VidForgeState)

    graph.add_node("understand", understand)
    graph.add_node("script", write_script)
    graph.add_node("storyboard", create_storyboard)
    graph.add_node("continuity", check_continuity)
    graph.add_node("prompts", create_prompts)
    graph.add_node("audio", plan_audio)
    graph.add_node("finalize", finish_planning)

    graph.add_edge(START, "understand")
    graph.add_edge("understand", "script")
    graph.add_edge("script", "storyboard")
    graph.add_edge("storyboard", "continuity")
    graph.add_edge("continuity", "prompts")
    graph.add_edge("prompts", "audio")
    graph.add_edge("audio", "finalize")
    graph.add_edge("finalize", END)

    return graph.compile()


video_planning_workflow = build_workflow()
