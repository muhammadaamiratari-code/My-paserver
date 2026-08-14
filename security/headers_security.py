"""HTTP security headers for Flask."""

from flask import Response

try:
    from .security_config import SecurityConfig
except ImportError:
    from security_config import SecurityConfig


def apply_security_headers(
    response: Response
) -> Response:

    response.headers.setdefault(
        "X-Content-Type-Options",
        "nosniff"
    )

    response.headers.setdefault(
        "X-Frame-Options",
        "DENY"
    )

    response.headers.setdefault(
        "Referrer-Policy",
        "strict-origin-when-cross-origin"
    )

    response.headers.setdefault(
        "Permissions-Policy",
        "camera=(self), "
        "microphone=(self), "
        "geolocation=(self)"
    )

    if SecurityConfig.ENABLE_CSP:

        response.headers.setdefault(
            "Content-Security-Policy",

            "default-src 'self'; "
            "base-uri 'self'; "
            "frame-ancestors 'none'; "
            "object-src 'none'; "
            "form-action 'self'; "
            "connect-src 'self'"
        )

    if SecurityConfig.ENABLE_HSTS:

        response.headers.setdefault(
            "Strict-Transport-Security",

            "max-age=31536000; "
            "includeSubDomains"
        )

    return response
