# ==========================================================================
# My AI Hub - Master Flask Backend
# Assistant: MyPA
#
# AI ROUTING:
#   1. Gemini = PRIMARY AI
#   2. OpenAI = FALLBACK AI
#   3. Friendly user message if BOTH fail
#
# CONNECTED FRONTEND:
#   index.html
#   style.css
#   script.js
#
# IMPORTANT:
#   Never put real API keys directly inside this file.
#   Use environment variables:
#
#       GEMINI_API_KEY
#       OPENAI_API_KEY
#       MYPA_MASTER_APP_CODE
#       MYPA_OWNER_TOKEN
#
# ==========================================================================

from flask import Flask, request, jsonify
from flask_cors import CORS

import os
import secrets
import logging
import traceback
import uuid
from datetime import datetime, timezone
from threading import Lock


# ==========================================================================
# 1. APP INITIALIZATION
# ==========================================================================

app = Flask(__name__)

CORS(app)


# ==========================================================================
# 2. APPLICATION INFORMATION
# ==========================================================================

APP_NAME = "My AI Hub"
ASSISTANT_NAME = "MyPA"


# ==========================================================================
# 3. AI CONFIGURATION
# ==========================================================================

# Gemini is PRIMARY.
GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY", "")

# Change this from environment variables if you want another model.
GEMINI_MODEL = os.environ.get(
    "GEMINI_MODEL",
    "gemini-3.6-flash"
)


# OpenAI is FALLBACK.
OPENAI_API_KEY = os.environ.get("OPENAI_API_KEY", "")

OPENAI_MODEL = os.environ.get(
    "OPENAI_MODEL",
    "gpt-5"
)


# ==========================================================================
# 4. SECURITY CONFIGURATION
# ==========================================================================

MASTER_APP_CODE = os.environ.get(
    "MYPA_MASTER_APP_CODE",
    "CHANGE-ME-BEFORE-PRODUCTION"
)

OWNER_TOKEN = os.environ.get(
    "MYPA_OWNER_TOKEN",
    "CHANGE-ME-OWNER-TOKEN"
)


# ==========================================================================
# 5. LOGGING
# ==========================================================================

logging.basicConfig(
    level=logging.INFO,
    format=(
        "%(asctime)s | "
        "%(levelname)s | "
        "%(name)s | "
        "%(message)s"
    )
)

logger = logging.getLogger("my_ai_hub")


# ==========================================================================
# 6. ONE-TIME SECURITY CODES
# ==========================================================================

DEFAULT_OTP_CODES = [
    "180090",
    "992811",
    "441029",
    "882301",
    "123450"
]

VALID_OTP_CODES = set(DEFAULT_OTP_CODES)

OTP_LOCK = Lock()


# ==========================================================================
# 7. FRIENDLY USER MESSAGES
# ==========================================================================

USER_MESSAGES = {

    "empty_message":
        "Please enter a message and try again.",

    "network":
        "We're having trouble connecting right now. Please check your internet connection and try again.",

    "ai_unavailable":
        "I'm unable to answer this right now. Please try again in a little while.",

    "topic_unavailable":
        "I'm unable to respond to this topic right now. Please try again later.",

    "login_failed":
        "We couldn't complete your login. Please check your details and try again.",

    "gmail_failed":
        "We couldn't verify this Gmail address. Please try again or use another Gmail address.",

    "invalid_app_code":
        "The application security code is not valid.",

    "invalid_otp":
        "This security code is invalid or has already been used.",

    "access_denied":
        "Access denied.",

    "server_error":
        "Something went wrong on our side. Please try again later."
}


# ==========================================================================
# 8. HELPER FUNCTIONS
# ==========================================================================

def utc_now():
    """Return current UTC time."""
    return datetime.now(timezone.utc).isoformat()


def create_error_id():
    """Create a unique error/request ID."""
    return f"ERR-{uuid.uuid4().hex[:10].upper()}"


def json_error(
    user_message,
    status_code=400,
    error_id=None,
    **extra
):
    """
    Return a clean response for the frontend.

    IMPORTANT:
    Technical traceback is NOT returned to normal users.
    """

    if error_id is None:
        error_id = create_error_id()

    response = {
        "status": "ERROR",
        "error": user_message,
        "error_id": error_id,
        "timestamp": utc_now()
    }

    response.update(extra)

    return jsonify(response), status_code


def get_json_body():
    """Safely read JSON body."""

    data = request.get_json(silent=True)

    if not isinstance(data, dict):
        return {}

    return data


def is_owner_authenticated(data):
    """
    Verify the server-side owner token.

    Do not trust a simple 'role=OWNER' value from the browser.
    """

    supplied_token = str(
        data.get("auth_token", "")
    )

    if not supplied_token:
        return False

    try:
        return secrets.compare_digest(
            supplied_token,
            OWNER_TOKEN
        )
    except Exception:
        return False


def get_request_role(data):
    """
    Determine whether this request belongs to Owner or User.

    IMPORTANT:
    Owner status is decided by authenticated token,
    not by a normal frontend role string.
    """

    if is_owner_authenticated(data):
        return "OWNER"

    return "USER"


def owner_error_response(
    error_id,
    endpoint,
    function_name,
    exception
):
    """
    Detailed developer information for authenticated Owner.

    Normal users never receive this information.
    """

    return {
        "status": "ERROR",
        "role": "OWNER",
        "error_id": error_id,
        "endpoint": endpoint,
        "function": function_name,
        "error_type": type(exception).__name__,
        "developer_error_log": str(exception),
        "traceback": traceback.format_exc(),
        "timestamp": utc_now()
    }


# ==========================================================================
# 9. GEMINI PRIMARY AI
# ==========================================================================

def ask_gemini(user_message):
    """
    Send the user's message to Gemini.

    Gemini is always attempted FIRST.
    """

    if not GEMINI_API_KEY:
        raise RuntimeError(
            "GEMINI_API_KEY is not configured."
        )

    try:
        from google import genai

        client = genai.Client(
            api_key=GEMINI_API_KEY
        )

        response = client.models.generate_content(
            model=GEMINI_MODEL,
            contents=user_message
        )

        answer = getattr(
            response,
            "text",
            None
        )

        if not answer:
            raise RuntimeError(
                "Gemini returned an empty response."
            )

        return answer.strip()

    except Exception as exc:

        logger.error(
            "Gemini request failed: %s",
            exc,
            exc_info=True
        )

        raise


# ==========================================================================
# 10. OPENAI FALLBACK AI
# ==========================================================================

def ask_openai(user_message):
    """
    Send the user's message to OpenAI.

    This function is only called if Gemini fails.
    """

    if not OPENAI_API_KEY:
        raise RuntimeError(
            "OPENAI_API_KEY is not configured."
        )

    try:
        from openai import OpenAI

        client = OpenAI(
            api_key=OPENAI_API_KEY
        )

        response = client.responses.create(
            model=OPENAI_MODEL,
            input=user_message
        )

        answer = getattr(
            response,
            "output_text",
            None
        )

        if not answer:
            raise RuntimeError(
                "OpenAI returned an empty response."
            )

        return answer.strip()

    except Exception as exc:

        logger.error(
            "OpenAI fallback request failed: %s",
            exc,
            exc_info=True
        )

        raise


# ==========================================================================
# 11. AI ROUTER
# ==========================================================================

def ask_ai_with_fallback(user_message):
    """
    AI priority:

        Gemini
          ↓
        OpenAI
          ↓
        Friendly failure

    Returns:
        answer, provider
    """

    # ------------------------------------------------------
    # FIRST: GEMINI
    # ------------------------------------------------------

    try:

        answer = ask_gemini(
            user_message
        )

        return answer, "Gemini"

    except Exception as gemini_error:

        logger.warning(
            "Gemini failed. Switching to OpenAI fallback."
        )

    # ------------------------------------------------------
    # SECOND: OPENAI
    # ------------------------------------------------------

    try:

        answer = ask_openai(
            user_message
        )

        return answer, "OpenAI"

    except Exception as openai_error:

        logger.error(
            "Both Gemini and OpenAI failed."
        )

        raise RuntimeError(
            "Both AI providers failed."
        ) from openai_error


# ==========================================================================
# 12. HOME / SERVER STATUS
# ==========================================================================

@app.route("/", methods=["GET"])
def home():

    return jsonify({
        "status": "Active",
        "app_name": APP_NAME,
        "assistant": ASSISTANT_NAME,
        "server": "Flask",
        "api": "Online",
        "ai_priority": [
            "Gemini",
            "OpenAI"
        ],
        "timestamp": utc_now()
    })


# ==========================================================================
# 13. HEALTH CHECK
# ==========================================================================

@app.route("/api/health", methods=["GET"])
def health_check():

    return jsonify({
        "status": "OK",
        "service": APP_NAME,
        "assistant": ASSISTANT_NAME,
        "gemini_configured": bool(GEMINI_API_KEY),
        "openai_configured": bool(OPENAI_API_KEY),
        "timestamp": utc_now()
    })


# ==========================================================================
# 14. STANDARD CHAT API
# ==========================================================================

@app.route("/api/chat", methods=["POST"])
def handle_chat():

    endpoint = "/api/chat"
    function_name = "handle_chat"

    data = get_json_body()

    user_message = str(
        data.get("message", "")
    ).strip()

    role = get_request_role(data)

    # ------------------------------------------------------
    # Empty message
    # ------------------------------------------------------

    if not user_message:

        return json_error(
            USER_MESSAGES["empty_message"],
            400
        )

    # ------------------------------------------------------
    # AI ROUTER
    # ------------------------------------------------------

    try:

        answer, provider = ask_ai_with_fallback(
            user_message
        )

        return jsonify({
            "status": "OK",
            "reply": answer,
            "assistant": ASSISTANT_NAME,
            "role": role,
            "provider": provider,
            "timestamp": utc_now()
        })

    except Exception as exc:

        error_id = create_error_id()

        logger.error(
            "AI request failed | "
            "error_id=%s | "
            "endpoint=%s | "
            "role=%s",
            error_id,
            endpoint,
            role,
            exc_info=True
        )

        # --------------------------------------------------
        # OWNER GETS FULL DIAGNOSTIC INFORMATION
        # --------------------------------------------------

        if role == "OWNER":

            response = owner_error_response(
                error_id=error_id,
                endpoint=endpoint,
                function_name=function_name,
                exception=exc
            )

            return jsonify(response), 503

        # --------------------------------------------------
        # USER GETS ONLY FRIENDLY ENGLISH MESSAGE
        # --------------------------------------------------

        return json_error(
            USER_MESSAGES["ai_unavailable"],
            503,
            error_id=error_id
        )


# ==========================================================================
# 15. OWNER AUTHENTICATION
# ==========================================================================

@app.route(
    "/api/auth/owner",
    methods=["POST"]
)
def authenticate_owner():

    data = get_json_body()

    passcode = str(
        data.get("passcode", "")
    )

    if not passcode:

        return json_error(
            "Please enter your owner passcode.",
            400
        )

    try:

        authenticated = secrets.compare_digest(
            passcode,
            OWNER_TOKEN
        )

    except Exception:

        authenticated = False

    if authenticated:

        return jsonify({
            "status": "OK",
            "authenticated": True,
            "role": "OWNER",
            "message": "Owner authentication successful.",
            "timestamp": utc_now()
        })

    return jsonify({
        "status": "ERROR",
        "authenticated": False,
        "role": "USER",
        "error": USER_MESSAGES["login_failed"],
        "timestamp": utc_now()
    }), 401


# ==========================================================================
# 16. REMOTE SECURITY COMMAND
# ==========================================================================

@app.route(
    "/api/security/remote-command",
    methods=["POST"]
)
def execute_remote_command():

    endpoint = "/api/security/remote-command"
    function_name = "execute_remote_command"

    data = get_json_body()

    app_code = str(
        data.get("app_code", "")
    ).strip()

    otp_code = str(
        data.get("otp_code", "")
    ).strip()

    command_type = str(
        data.get("command_type", "")
    ).strip()

    role = get_request_role(data)

    # ------------------------------------------------------
    # App Code
    # ------------------------------------------------------

    try:

        valid_app_code = secrets.compare_digest(
            app_code,
            MASTER_APP_CODE
        )

    except Exception:

        valid_app_code = False

    if not valid_app_code:

        return json_error(
            USER_MESSAGES["invalid_app_code"],
            401
        )

    # ------------------------------------------------------
    # OTP
    # ------------------------------------------------------

    with OTP_LOCK:

        if otp_code not in VALID_OTP_CODES:

            return json_error(
                USER_MESSAGES["invalid_otp"],
                403
            )

        # Burn immediately.
        VALID_OTP_CODES.remove(
            otp_code
        )

    # ------------------------------------------------------
    # SAFE FOLDER
    # ------------------------------------------------------

    if command_type == "WIPE_SAFE_FOLDER":

        return jsonify({
            "status": "Success",
            "command": "WIPE_SAFE_FOLDER",
            "message": (
                "Security command accepted. "
                "The one-time security code has been consumed."
            ),
            "ai_visibility": "0%",
            "otp_burned": True,
            "timestamp": utc_now()
        })

    # ------------------------------------------------------
    # LOCATION
    # ------------------------------------------------------

    if command_type == "ANTI_THEFT_LOC":

        return jsonify({
            "status": "Success",
            "command": "ANTI_THEFT_LOC",
            "action": "FETCH_LOCATION",
            "message": (
                "The authorized location request has been accepted."
            ),
            "otp_burned": True,
            "timestamp": utc_now()
        })

    # ------------------------------------------------------
    # SECURITY STATUS
    # ------------------------------------------------------

    if command_type == "SECURITY_STATUS":

        with OTP_LOCK:

            remaining_codes = len(
                VALID_OTP_CODES
            )

        return jsonify({
            "status": "Success",
            "command": "SECURITY_STATUS",
            "remaining_one_time_codes": remaining_codes,
            "otp_burned": True,
            "timestamp": utc_now()
        })

    # ------------------------------------------------------
    # UNKNOWN COMMAND
    # ------------------------------------------------------

    return jsonify({
        "status": "Success",
        "command": command_type,
        "message": (
            "The security command was accepted, "
            "but this command is not configured yet."
        ),
        "otp_burned": True,
        "timestamp": utc_now()
    })


# ==========================================================================
# 17. SECURITY STATUS
# ==========================================================================

@app.route(
    "/api/security/status",
    methods=["GET"]
)
def security_status():

    with OTP_LOCK:

        remaining_codes = len(
            VALID_OTP_CODES
        )

    return jsonify({
        "status": "OK",
        "security_engine": "Active",
        "app_name": APP_NAME,
        "assistant": ASSISTANT_NAME,
        "remaining_one_time_codes": remaining_codes,
        "timestamp": utc_now()
    })


# ==========================================================================
# 18. GENERATE NEW ONE-TIME CODE
# ==========================================================================

@app.route(
    "/api/security/generate-code",
    methods=["POST"]
)
def generate_security_code():

    data = get_json_body()

    # Only authenticated Owner can generate a code.
    if not is_owner_authenticated(data):

        return json_error(
            USER_MESSAGES["access_denied"],
            401
        )

    new_code = (
        f"{secrets.randbelow(1000000):06d}"
    )

    with OTP_LOCK:

        VALID_OTP_CODES.add(
            new_code
        )

    return jsonify({
        "status": "Success",
        "message": (
            "A new one-time security code "
            "has been generated."
        ),
        "code": new_code,
        "one_time": True,
        "timestamp": utc_now()
    })


# ==========================================================================
# 19. ERROR HANDLERS
# ==========================================================================

@app.errorhandler(404)
def not_found(error):

    return json_error(
        "The requested service could not be found.",
        404
    )


@app.errorhandler(405)
def method_not_allowed(error):

    return json_error(
        "This request method is not supported.",
        405
    )


@app.errorhandler(500)
def internal_server_error(error):

    error_id = create_error_id()

    logger.error(
        "Internal server error | error_id=%s",
        error_id,
        exc_info=True
    )

    return json_error(
        USER_MESSAGES["server_error"],
        500,
        error_id=error_id
    )


# ==========================================================================
# 20. APPLICATION START
# ==========================================================================

if __name__ == "__main__":

    port = int(
        os.environ.get(
            "PORT",
            5000
        )
    )

    app.run(
        host="0.0.0.0",
        port=port,
        debug=False
    )
