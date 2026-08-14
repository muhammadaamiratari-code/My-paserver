"""Server-side authorization helpers.

Never trust a role supplied directly by
the browser. The role must come from a
server-verified identity/session.
"""

from functools import wraps

from flask import (
    g,
    jsonify
)


def set_authenticated_identity(
    *,
    user_id: str,
    role: str
) -> None:

    g.user_id = user_id
    g.user_role = role.upper()


def require_auth(view):

    @wraps(view)
    def wrapped(
        *args,
        **kwargs
    ):

        if not getattr(
            g,
            "user_id",
            None
        ):

            return jsonify({

                "status":
                    "ERROR",

                "code":
                    "AUTH_REQUIRED",

                "message":
                    "Authentication is required."

            }), 401

        return view(
            *args,
            **kwargs
        )

    return wrapped


def require_owner(view):

    @wraps(view)
    def wrapped(
        *args,
        **kwargs
    ):

        if not getattr(
            g,
            "user_id",
            None
        ):

            return jsonify({

                "status":
                    "ERROR",

                "code":
                    "AUTH_REQUIRED",

                "message":
                    "Authentication is required."

            }), 401

        if getattr(
            g,
            "user_role",
            "USER"
        ) != "OWNER":

            return jsonify({

                "status":
                    "ERROR",

                "code":
                    "OWNER_REQUIRED",

                "message":
                    "Owner authorization is required."

            }), 403

        return view(
            *args,
            **kwargs
        )

    return wrapped
