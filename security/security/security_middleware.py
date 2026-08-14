"""Flask security middleware.

This is an additional defensive layer.
It does not replace real authentication,
HTTPS, a database, or a shared production
rate-limit store.
"""

import secrets
import time

from flask import (
    Flask,
    g,
    jsonify,
    request
)

try:

    from .audit_log import audit_event
    from .csrf_security import origin_allowed
    from .headers_security import (
        apply_security_headers
    )
    from .rate_limit import RateLimiter
    from .security_config import (
        SecurityConfig
    )

except ImportError:

    from audit_log import audit_event
    from csrf_security import origin_allowed
    from headers_security import (
        apply_security_headers
    )
    from rate_limit import RateLimiter
    from security_config import (
        SecurityConfig
    )


class SecurityMiddleware:

    def __init__(
        self,
        app: Flask | None = None
    ):

        self.limiter = RateLimiter(

            SecurityConfig
            .RATE_LIMIT_MAX_REQUESTS,

            SecurityConfig
            .RATE_LIMIT_WINDOW_SECONDS
        )

        if app is not None:
            self.init_app(app)

    def init_app(
        self,
        app: Flask
    ):

        app.config[
            "MAX_CONTENT_LENGTH"
        ] = (
            SecurityConfig
            .MAX_JSON_BYTES
        )

        @app.before_request
        def before_request():

            g.request_id = (
                secrets.token_urlsafe(12)
            )

            g.request_started = (
                time.time()
            )

            if not origin_allowed():

                audit_event(

                    "blocked_origin",

                    request_id=
                        g.request_id,

                    outcome=
                        "denied",

                    details={

                        "path":
                            request.path,

                        "method":
                            request.method
                    }
                )

                return jsonify({

                    "status":
                        "ERROR",

                    "code":
                        "ORIGIN_BLOCKED",

                    "message":
                        "This request origin is not allowed."

                }), 403

            client_key = (
                request.headers.get(
                    "X-Forwarded-For",
                    request.remote_addr
                    or "unknown"
                )
            )

            client_key = (
                client_key
                .split(",")[0]
                .strip()
            )

            if not self.limiter.allow(
                f"{client_key}:{request.path}"
            ):

                audit_event(

                    "rate_limit",

                    request_id=
                        g.request_id,

                    outcome=
                        "denied",

                    details={

                        "path":
                            request.path
                    }
                )

                return jsonify({

                    "status":
                        "ERROR",

                    "code":
                        "RATE_LIMITED",

                    "message":
                        "Too many requests. Please try again later."

                }), 429

            if request.is_json:

                data = request.get_json(
                    silent=True
                )

                if data is None:

                    return jsonify({

                        "status":
                            "ERROR",

                        "code":
                            "INVALID_JSON",

                        "message":
                            "The request body is not valid JSON."

                    }), 400

                message = data.get(
                    "message"
                )

                if (
                    isinstance(
                        message,
                        str
                    )
                    and
                    len(message)
                    >
                    SecurityConfig
                    .MAX_MESSAGE_CHARS
                ):

                    return jsonify({

                        "status":
                            "ERROR",

                        "code":
                            "MESSAGE_TOO_LARGE",

                        "message":
                            "The message is too long."

                    }), 413

        @app.after_request
        def after_request(
            response
        ):

            response = (
                apply_security_headers(
                    response
                )
            )

            response.headers[
                "X-Request-ID"
            ] = getattr(
                g,
                "request_id",
                "-"
            )

            return response

        @app.errorhandler(413)
        def request_too_large(
            _error
        ):

            return jsonify({

                "status":
                    "ERROR",

                "code":
                    "PAYLOAD_TOO_LARGE",

                "message":
                    "The request is too large."

            }), 413

        @app.errorhandler(500)
        def internal_error(
            _error
        ):

            request_id = getattr(
                g,
                "request_id",
                "-"
            )

            audit_event(

                "internal_error",

                request_id=
                    request_id,

                outcome=
                    "error",

                details={

                    "path":
                        request.path
                }
            )

            return jsonify({

                "status":
                    "ERROR",

                "code":
                    "INTERNAL_ERROR",

                "message":
                    "The service is temporarily unavailable. Please try again."

            }), 500

        return app
