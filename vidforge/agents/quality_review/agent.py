from typing import Any


class AIQualityReviewAgent:
    name = "ai_quality_review"

    def run(
        self,
        user_request: str,
        generation_result: dict[str, Any],
        technical_qc: dict[str, Any],
    ) -> dict[str, Any]:
        issues = []

        if not user_request.strip():
            issues.append("Missing original user request")

        if not generation_result:
            issues.append("Missing generation result")

        if not technical_qc:
            issues.append("Missing technical QC result")
        elif technical_qc.get("status") not in {"PASS", "PASSED"}:
            issues.append("Technical QC has not passed")

        passed = not issues

        return {
            "review_status": "READY_FOR_FINAL_DECISION" if passed else "NEEDS_REVIEW",
            "checks_completed": [
                "request_present",
                "generation_result_present",
                "technical_qc_status",
            ],
            "issues": issues,
            "semantic_review_completed": False,
        }
