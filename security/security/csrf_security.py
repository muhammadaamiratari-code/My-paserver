"""Origin / CSRF-style request protection.

For cookie-authenticated browser sessions,
use a real CSRF token mechanism as well.
"""

from flask import request

try:
    from .security_config import SecurityConfig
except ImportError:
    from security_config import SecurityConfig


SAFE_METHODS = {
    "GET",
    "HEAD",
    "OPTIONS"
}


def origin_allowed() -> bool:

    if request.method in SAFE_METHODS:
        return True

    if not SecurityConfig.TRUSTED_ORIGINS:

        return (
            SecurityConfig.APP_ENV
            != "production"
        )

    origin = request.headers.get(
        "Origin"
    )

    return bool(
        origin
        and
        origin in SecurityConfig.TRUSTED_ORIGINS
    )
