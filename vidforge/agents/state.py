from typing import Any, TypedDict


class VidForgeState(TypedDict, total=False):
    job_id: int
    project_id: int
    user_id: int

    user_request: str
    duration_seconds: int
    style: str

    characters: list[dict[str, Any]]
    understood_request: dict[str, Any]
    script: dict[str, Any]
    storyboard: list[dict[str, Any]]
    continuity_report: dict[str, Any]
    prompts: list[dict[str, Any]]
    audio_plan: dict[str, Any]

    router_result: dict[str, Any]
    generation_result: dict[str, Any]
    technical_qc: dict[str, Any]
    ai_quality_review: dict[str, Any]

    retry_count: int
    status: str
    errors: list[str]
