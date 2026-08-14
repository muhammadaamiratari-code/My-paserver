"""Central security configuration.

Sensitive values must come from environment variables.
Never store passwords, API keys, OTPs, or master tokens here.
"""

import os


def env_bool(name: str, default: bool = False) -> bool:
    value = os.getenv(name)

    if value is None:
        return default

    return value.strip().lower() in {
        "1",
        "true",
        "yes",
        "on",
    }


class SecurityConfig:

    APP_ENV = os.getenv(
        "APP_ENV",
        "production"
    ).lower()

    MAX_JSON_BYTES = int(
        os.getenv(
            "MAX_JSON_BYTES",
            "1048576"
        )
    )

    MAX_MESSAGE_CHARS = int(
        os.getenv(
            "MAX_MESSAGE_CHARS",
            "12000"
        )
    )

    RATE_LIMIT_WINDOW_SECONDS = int(
        os.getenv(
            "RATE_LIMIT_WINDOW_SECONDS",
            "60"
        )
    )

    RATE_LIMIT_MAX_REQUESTS = int(
        os.getenv(
            "RATE_LIMIT_MAX_REQUESTS",
            "30"
        )
    )

    OTP_MAX_ATTEMPTS = int(
        os.getenv(
            "OTP_MAX_ATTEMPTS",
            "5"
        )
    )

    OTP_LOCK_SECONDS = int(
        os.getenv(
            "OTP_LOCK_SECONDS",
            "900"
        )
    )

    SESSION_TTL_SECONDS = int(
        os.getenv(
            "SESSION_TTL_SECONDS",
            "3600"
        )
    )

    ENABLE_HSTS = env_bool(
        "ENABLE_HSTS",
        True
    )

    ENABLE_CSP = env_bool(
        "ENABLE_CSP",
        True
    )

    TRUSTED_ORIGINS = {
        origin.strip()
        for origin in os.getenv(
            "TRUSTED_ORIGINS",
            ""
        ).split(",")
        if origin.strip()
    }
