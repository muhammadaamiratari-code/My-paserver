"""Security audit logging.

Never record passwords, API keys, OTP values,
authorization headers, or sensitive file contents.
"""

import json
import logging

from datetime import datetime, timezone


logger = logging.getLogger(
    "mya.security"
)


def audit_event(
    event: str,
    *,
    request_id: str = "-",
    actor: str = "unknown",
    outcome: str = "info",
    details: dict | None = None
) -> None:

    safe_details = details or {}

    record = {

        "timestamp":
            datetime.now(
                timezone.utc
            ).isoformat(),

        "event":
            event,

        "request_id":
            request_id,

        "actor":
            actor,

        "outcome":
            outcome,

        "details":
            safe_details,
    }

    logger.info(
        json.dumps(
            record,
            ensure_ascii=False,
            default=str
        )
    )
