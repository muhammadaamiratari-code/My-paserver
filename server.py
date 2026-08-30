import os
import base64
import json
import urllib.request
import urllib.error

from flask import Flask, render_template, request, jsonify
from flask_cors import CORS

from google import genai
from google.genai import types

from truth_policy import (
    TRUTH_POLICY,
    PROFESSIONAL_DEVELOPER_POLICY,
    PROJECT_MEMORY_POLICY,
    WORKFLOW_POLICY,
    SECURITY_POLICY,
    GENERAL_CONVERSATION_POLICY,
    FACT_CHECKING_LIMITATION,
    USER_PERSONAL_INSTRUCTIONS,
)


# ============================================================
# LOCAL DEVELOPER MEMORY
# ============================================================

# Local memory کو API layer سے الگ رکھا گیا ہے۔
# اگر local_memory.py میں عارضی مسئلہ ہو تو server پھر بھی start ہو سکے۔
try:
    import local_memory

    local_memory.initialize_database()

    LOCAL_MEMORY_AVAILABLE = True
    LOCAL_MEMORY_ERROR = ""

except Exception as exc:
    local_memory = None
    LOCAL_MEMORY_AVAILABLE = False
    LOCAL_MEMORY_ERROR = str(exc)


# ============================================================
# DEVELOPMENT / TESTING TOOLS
#
# These modules are connected without changing their code.
# The API below exposes their existing functions through
# the server.
# ============================================================

try:
    import dev_tools
    import backup_manager
    import testing_manager
    import browser_testing
    import git_manager

    DEVELOPMENT_TOOLS_AVAILABLE = True
    DEVELOPMENT_TOOLS_ERROR = ""

except Exception as exc:
    dev_tools = None
    backup_manager = None
    testing_manager = None
    browser_testing = None
    git_manager = None

    DEVELOPMENT_TOOLS_AVAILABLE = False
    DEVELOPMENT_TOOLS_ERROR = str(exc)


app = Flask(__name__)


# ============================================================
# CORS
# ============================================================

# CORS policy unchanged.
CORS(app)


# ============================================================
# API CONFIGURATION
# ============================================================

# ------------------------------------------------------------
# OPENROUTER
#
# Text:
#   Primary   = MiniMax M3
#   Secondary = Nemotron 3 Ultra
#
# Image:
#   Primary  = Gemma 4 31B
#   Fallback = Gemini
# ------------------------------------------------------------

OPENROUTER_API_KEY = os.environ.get(
    "OPENROUTER_API_KEY",
    ""
)

OPENROUTER_PRIMARY_MODEL = os.environ.get(
    "OPENROUTER_PRIMARY_MODEL",
    "minimax/minimax-m3:free"
)

OPENROUTER_SECONDARY_MODEL = os.environ.get(
    "OPENROUTER_SECONDARY_MODEL",
    "nvidia/nemotron-3-ultra-550b-a55b:free"
)

OPENROUTER_VISION_MODEL = os.environ.get(
    "OPENROUTER_VISION_MODEL",
    "google/gemma-4-31b-it:free"
)


# Text primary timeout = 20 seconds
OPENROUTER_PRIMARY_TIMEOUT = int(
    os.environ.get(
        "OPENROUTER_PRIMARY_TIMEOUT",
        "20"
    )
)


# Text secondary timeout = 20 seconds
OPENROUTER_SECONDARY_TIMEOUT = int(
    os.environ.get(
        "OPENROUTER_SECONDARY_TIMEOUT",
        "20"
    )
)


# Image primary timeout = 20 seconds
OPENROUTER_VISION_TIMEOUT = int(
    os.environ.get(
        "OPENROUTER_VISION_TIMEOUT",
        "20"
    )
)


# ------------------------------------------------------------
# GEMINI
#
# Final fallback for text and image
# Timeout = 15 seconds
# ------------------------------------------------------------

GEMINI_API_KEY = os.environ.get(
    "GEMINI_API_KEY",
    "YOUR_GEMINI_API_KEY_HERE"
)

GEMINI_MODEL = os.environ.get(
    "GEMINI_MODEL",
    "gemini-3.6-flash"
)

GEMINI_TIMEOUT_MS = int(
    os.environ.get(
        "GEMINI_TIMEOUT_MS",
        "15000"
    )
)


client = None
if (
    GEMINI_API_KEY
    and GEMINI_API_KEY != "YOUR_GEMINI_API_KEY_HERE"
):
    try:
        client = genai.Client(
            api_key=GEMINI_API_KEY,
            http_options=types.HttpOptions(
                timeout=float(
                    GEMINI_TIMEOUT_MS / 1000.0
                )
            )
        )
    except Exception:
        client = None


# ============================================================
# SYSTEM INSTRUCTION
#
# Existing policies are preserved.
# ============================================================

SYSTEM_INSTRUCTION = (
    TRUTH_POLICY
    + "\n\n"
    + PROFESSIONAL_DEVELOPER_POLICY
    + "\n\n"
    + PROJECT_MEMORY_POLICY
    + "\n\n"
    + WORKFLOW_POLICY
    + "\n\n"
    + SECURITY_POLICY
    + "\n\n"
    + GENERAL_CONVERSATION_POLICY
    + "\n\n"
    + FACT_CHECKING_LIMITATION
    + "\n\n"
    + USER_PERSONAL_INSTRUCTIONS
)


# ============================================================
# LOCAL MEMORY HELPERS
# ============================================================

def _memory_project_id():
    """
    Active local-memory project کا ID حاصل کرتا ہے۔
    Memory unavailable ہو تو server کو crash نہیں ہونے دیتا۔
    """

    if not LOCAL_MEMORY_AVAILABLE:
        return None

    try:
        project = local_memory.get_active_project()

        if project:
            return project["id"]

        return None

    except Exception:
        return None


def _memory_context():
    """
    Active project کی developer memory AI کو فراہم کرتا ہے۔

    Memory کو 30000 characters تک محدود رکھا گیا ہے تاکہ
    API request غیر ضروری طور پر بہت بڑی نہ ہو۔
    """

    if not LOCAL_MEMORY_AVAILABLE:
        return ""

    try:
        project = local_memory.get_active_project()

        if not project:
            return ""

        context = local_memory.get_project_context(
            project["id"]
        )

        if not context:
            return ""

        text = json.dumps(
            context,
            ensure_ascii=False,
            default=str
        )

        return text[:30000]

    except Exception:
        return ""


def _system_with_memory():
    """
    Existing system policies کے ساتھ active local
    developer memory شامل کرتا ہے۔
    """

    context = _memory_context()

    if not context:
        return SYSTEM_INSTRUCTION

    return (
        SYSTEM_INSTRUCTION
        + "\n\n"
        + "=== LOCAL DEVELOPER MEMORY CONTEXT ===\n"
        + context
        + "\n"
        + "=== END LOCAL DEVELOPER MEMORY CONTEXT ==="
    )


def _remember_message(
    role,
    content,
    project_id=None
):
    """
    Chat message کو local SQLite memory میں محفوظ کرتا ہے۔
    """

    if not LOCAL_MEMORY_AVAILABLE:
        return False

    try:
        pid = (
            project_id
            if project_id is not None
            else _memory_project_id()
        )

        if pid is None:
            return False

        local_memory.add_chat_message(
            pid,
            role,
            str(content)
        )

        return True

    except Exception:
        return False


def _remember_change(
    summary,
    file_path="server.py",
    result=""
):
    """
    Server/API related change کو local developer memory
    میں record کرنے کے لیے helper۔
    """

    if not LOCAL_MEMORY_AVAILABLE:
        return False

    try:
        pid = _memory_project_id()

        if pid is None:
            return False

        local_memory.record_change(
            pid,
            summary=summary,
            file_path=file_path,
            reason="Server/API routing or memory integration",
            result=result
        )

        return True

    except Exception:
        return False


# ============================================================
# DEVELOPMENT / TESTING TOOL HELPERS
# ============================================================

def _development_tools_ready():
    return (
        DEVELOPMENT_TOOLS_AVAILABLE
        and dev_tools is not None
        and backup_manager is not None
        and testing_manager is not None
        and browser_testing is not None
        and git_manager is not None
    )


def _tool_result(module, function_name, *args, **kwargs):
    if not _development_tools_ready():
        return (
            False,
            "Development/testing tools unavailable: "
            + DEVELOPMENT_TOOLS_ERROR
        )

    try:
        function = getattr(module, function_name)
    except AttributeError:
        return (
            False,
            f"Tool function not available: {function_name}"
        )

    try:
        return function(*args, **kwargs)

    except Exception as exc:
        return (
            False,
            f"{function_name} میں مسئلہ: {exc}"
        )


# ============================================================
# DEVELOPMENT TOOL API
# ============================================================

@app.route(
    "/api/dev-tools",
    methods=["POST"]
)
def development_tools():

    data = request.get_json(
        silent=True
    )

    if not isinstance(
        data,
        dict
    ):
        data = {}

    tool = str(
        data.get(
            "tool",
            ""
        )
    ).strip()

    action = str(
        data.get(
            "action",
            ""
        )
    ).strip()

    if not tool or not action:
        return jsonify({
            "success": False,
            "reply": (
                "Development tool اور action ضروری ہیں۔"
            )
        }), 400

    # --------------------------------------------------------
    # dev_tools.py
    # --------------------------------------------------------

    if tool == "dev_tools":

        if action == "read_file":
            success, result = _tool_result(
                dev_tools,
                "read_file",
                data.get("file_path", "")
            )

        elif action == "find_errors":
            success, result = _tool_result(
                dev_tools,
                "find_errors",
                data.get("file_path", "")
            )

        elif action == "edit_file":
            success, result = _tool_result(
                dev_tools,
                "edit_file",
                data.get("file_path", ""),
                data.get("old_text", ""),
                data.get("new_text", ""),
                data.get("create_backup", True)
            )

        elif action == "add_code":
            success, result = _tool_result(
                dev_tools,
                "add_code",
                data.get("file_path", ""),
                data.get("new_code", ""),
                data.get("position", "end"),
                data.get("create_backup", True)
            )

        elif action == "list_project_files":
            success, result = _tool_result(
                dev_tools,
                "list_project_files",
                data.get("extensions")
            )

        else:
            return jsonify({
                "success": False,
                "reply": (
                    f"Unknown dev_tools action: {action}"
                )
            }), 400

    # --------------------------------------------------------
    # backup_manager.py
    # --------------------------------------------------------

    elif tool == "backup_manager":

        if action == "create_backup":
            success, result = _tool_result(
                backup_manager,
                "create_backup",
                data.get("file_path", ""),
                data.get("reason", "")
            )

        elif action == "restore_backup":
            success, result = _tool_result(
                backup_manager,
                "restore_backup",
                data.get("backup_path", "")
            )

        elif action == "list_backups":
            success, result = _tool_result(
                backup_manager,
                "list_backups",
                data.get("file_path")
            )

        else:
            return jsonify({
                "success": False,
                "reply": (
                    f"Unknown backup_manager action: {action}"
                )
            }), 400

    # --------------------------------------------------------
    # testing_manager.py
    # --------------------------------------------------------

    elif tool == "testing_manager":

        if action == "run_python_syntax":
            success, result = _tool_result(
                testing_manager,
                "run_python_syntax",
                data.get("file_path", "")
            )

        elif action == "run_python_file":
            success, result = _tool_result(
                testing_manager,
                "run_python_file",
                data.get("file_path", ""),
                data.get("args")
            )

        elif action == "check_server_health":
            success, result = _tool_result(
                testing_manager,
                "check_server_health",
                data.get(
                    "url",
                    "http://127.0.0.1:5000/api/health"
                )
            )

        elif action == "get_test_history":
            success, result = _tool_result(
                testing_manager,
                "get_test_history",
                data.get("test_type"),
                data.get("target")
            )

        else:
            return jsonify({
                "success": False,
                "reply": (
                    f"Unknown testing_manager action: {action}"
                )
            }), 400

    # --------------------------------------------------------
    # browser_testing.py
    # --------------------------------------------------------

    elif tool == "browser_testing":

        if action == "open_browser":
            success, result = _tool_result(
                browser_testing,
                "open_browser",
                data.get("url", "")
            )

        elif action == "check_page_status":
            success, result = _tool_result(
                browser_testing,
                "check_page_status",
                data.get("url", ""),
                data.get("expected_status", 200)
            )

        elif action == "test_form":
            success, result = _tool_result(
                browser_testing,
                "test_form",
                data.get("url", ""),
                data.get("form_data", {}),
                data.get("method", "POST")
            )

        else:
            return jsonify({
                "success": False,
                "reply": (
                    f"Unknown browser_testing action: {action}"
                )
            }), 400

    # --------------------------------------------------------
    # git_manager.py
    # --------------------------------------------------------

    elif tool == "git_manager":

        if action == "git_status":
            success, result = _tool_result(
                git_manager,
                "git_status",
                data.get("repo_path")
            )

        elif action == "git_diff":
            success, result = _tool_result(
                git_manager,
                "git_diff",
                data.get("file_path"),
                data.get("repo_path")
            )

        elif action == "git_add":
            success, result = _tool_result(
                git_manager,
                "git_add",
                data.get("file_path", ""),
                data.get("repo_path")
            )

        elif action == "git_commit":
            success, result = _tool_result(
                git_manager,
                "git_commit",
                data.get("message", ""),
                data.get("repo_path")
            )

        elif action == "git_push":
            success, result = _tool_result(
                git_manager,
                "git_push",
                data.get("branch", "main"),
                data.get("repo_path")
            )

        elif action == "git_pull":
            success, result = _tool_result(
                git_manager,
                "git_pull",
                data.get("branch", "main"),
                data.get("repo_path")
            )

        elif action == "git_log":
            success, result = _tool_result(
                git_manager,
                "git_log",
                data.get("limit", 10),
                data.get("repo_path")
            )

        elif action == "git_create_branch":
            success, result = _tool_result(
                git_manager,
                "git_create_branch",
                data.get("branch_name", ""),
                data.get("repo_path")
            )

        else:
            return jsonify({
                "success": False,
                "reply": (
                    f"Unknown git_manager action: {action}"
                )
            }), 400

    else:
        return jsonify({
            "success": False,
            "reply": (
                f"Unknown development tool: {tool}"
            )
        }), 400

    return jsonify({
        "success": bool(success),
        "tool": tool,
        "action": action,
        "result": result
    }), (200 if success else 400)


# ============================================================
# OPENROUTER REQUEST HELPER
# ============================================================

def openrouter_request(
    model,
    messages,
    timeout_seconds
):
    if not OPENROUTER_API_KEY:
        raise RuntimeError(
            "OpenRouter API is not configured."
        )

    payload = {
        "model": model,
        "messages": messages
    }

    body = json.dumps(
        payload
    ).encode("utf-8")

    req = urllib.request.Request(
        "https://openrouter.ai/api/v1/chat/completions",
        data=body,
        headers={
            "Authorization": (
                f"Bearer {OPENROUTER_API_KEY}"
            ),
            "Content-Type": "application/json",
        },
        method="POST"
    )

    with urllib.request.urlopen(
        req,
        timeout=timeout_seconds
    ) as response:

        raw = response.read().decode(
            "utf-8"
        )

    result = json.loads(raw)

    choices = result.get(
        "choices",
        []
    )

    if not choices:
        raise RuntimeError(
            "OpenRouter returned no choices."
        )

    message = choices[0].get(
        "message",
        {}
    )

    answer = message.get(
        "content"
    )

    if isinstance(answer, list):

        text_parts = []

        for part in answer:

            if isinstance(part, dict):

                text = part.get(
                    "text"
                )

                if text:
                    text_parts.append(
                        str(text)
                    )

        answer = "\n".join(
            text_parts
        )

    if not answer:
        raise RuntimeError(
            "OpenRouter returned an empty response."
        )

    return str(answer).strip()


# ============================================================
# GEMINI REQUEST HELPER
# ============================================================

def gemini_request(contents):

    if not client:
        raise RuntimeError(
            "Gemini API is not configured."
        )

    response = client.models.generate_content(
        model=GEMINI_MODEL,
        contents=contents,
        config=types.GenerateContentConfig(
            system_instruction=_system_with_memory()
        )
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


# ============================================================
# HOME
# ============================================================

@app.route("/")
def index():
    return render_template(
        "index.html"
    )


# ============================================================
# HEALTH
# ============================================================

@app.route(
    "/api/health",
    methods=["GET"]
)
def health():

    return jsonify({

        "assistant": "AI Assistant",

        "service": "AI Assistant",

        "status": "OK",

        "openrouter_configured": bool(
            OPENROUTER_API_KEY
        ),

        "gemini_configured": bool(
            client
        ),

        "local_memory_available": (
            LOCAL_MEMORY_AVAILABLE
        ),

        "development_tools_available": (
            DEVELOPMENT_TOOLS_AVAILABLE
        ),

        "development_tools_error": (
            DEVELOPMENT_TOOLS_ERROR
        ),

        "primary_model": (
            OPENROUTER_PRIMARY_MODEL
        ),

        "secondary_model": (
            OPENROUTER_SECONDARY_MODEL
        ),

        "vision_model": (
            OPENROUTER_VISION_MODEL
        ),

        "primary_timeout": (
            OPENROUTER_PRIMARY_TIMEOUT
        ),

        "secondary_timeout": (
            OPENROUTER_SECONDARY_TIMEOUT
        ),

        "vision_timeout": (
            OPENROUTER_VISION_TIMEOUT
        ),

        "gemini_model": (
            GEMINI_MODEL
        ),

        "gemini_timeout_seconds": (
            GEMINI_TIMEOUT_MS / 1000.0
        ),
    })


# ============================================================
# CHAT API
# ============================================================

@app.route(
    "/api/chat",
    methods=["POST"]
)
def chat():

    project_id = _memory_project_id()

    try:

        # ----------------------------------------------------
        # Read request
        # ----------------------------------------------------

        data = request.get_json(
            silent=True
        )

        if not isinstance(
            data,
            dict
        ):
            data = {}

        user_message = str(
            data.get(
                "message",
                ""
            )
        ).strip()

        history = data.get(
            "history",
            []
        )

        image_data = data.get(
            "image",
            None
        )

        # ----------------------------------------------------
        # Empty message check
        # ----------------------------------------------------

        if not user_message:

            return jsonify({
                "reply": "Please enter a message.",
                "success": False
            }), 400


        # ----------------------------------------------------
        # Save user message to local memory
        # ----------------------------------------------------

        _remember_message(
            "user",
            user_message,
            project_id
        )


        # ====================================================
        # IMAGE / SCREENSHOT ROUTING
        # ====================================================

        if image_data:

            try:

                image_string = image_data
                mime_type = "image/jpeg"

                if "," in image_string:
                    header, image_string = image_string.split(",", 1)
                    if "data:" in header and ";base64" in header:
                        mime_type = header.split(";")[0].replace("data:", "")

                image_bytes = (
                    base64.b64decode(
                        image_string
                    )
                )

            except Exception:

                return jsonify({
                    "reply": (
                        "The image could not be processed."
                    ),
                    "success": False
                }), 400


            # ------------------------------------------------
            # Convert image to data URL
            # ------------------------------------------------

            image_data_url = (
                f"data:{mime_type};base64,"
                + base64.b64encode(
                    image_bytes
                ).decode("utf-8")
            )


            vision_messages = [

                {
                    "role": "system",
                    "content": _system_with_memory()
                },

                {
                    "role": "user",
                    "content": [

                        {
                            "type": "text",
                            "text": user_message
                        },

                        {
                            "type": "image_url",
                            "image_url": {
                                "url": image_data_url
                            }
                        }

                    ]
                }

            ]


            # ------------------------------------------------
            # Gemma 4 31B = IMAGE PRIMARY
            # Timeout = 20 seconds
            # ------------------------------------------------

            try:

                answer = openrouter_request(
                    OPENROUTER_VISION_MODEL,
                    vision_messages,
                    OPENROUTER_VISION_TIMEOUT
                )

                _remember_message(
                    "assistant",
                    answer,
                    project_id
                )

                return jsonify({

                    "reply": answer,

                    "success": True,

                    "provider": (
                        "Gemma 4 31B"
                    )

                })

            except Exception:

                pass


            # ------------------------------------------------
            # Gemini = IMAGE FINAL FALLBACK
            # Timeout = 15 seconds
            # ------------------------------------------------

            try:

                formatted_contents = []


                if isinstance(
                    history,
                    list
                ):

                    for item in history:

                        if not isinstance(
                            item,
                            dict
                        ):
                            continue

                        text = str(
                            item.get(
                                "text",
                                ""
                            )
                        ).strip()

                        if not text:
                            continue

                        sender = item.get(
                            "sender",
                            "user"
                        )

                        role = (
                            "user"
                            if sender == "user"
                            else "model"
                        )

                        formatted_contents.append(

                            types.Content(

                                role=role,

                                parts=[

                                    types.Part.from_text(
                                        text=text
                                    )

                                ]

                            )

                        )


                formatted_contents.append(

                    types.Content(

                        role="user",

                        parts=[

                            types.Part.from_text(
                                text=user_message
                            ),

                            types.Part.from_bytes(
                                data=image_bytes,
                                mime_type=mime_type
                            )

                        ]

                    )

                )


                answer = gemini_request(
                    formatted_contents
                )


                _remember_message(
                    "assistant",
                    answer,
                    project_id
                )


                return jsonify({

                    "reply": answer,

                    "success": True,

                    "provider": "Gemini"

                })


            except Exception as exc:

                return jsonify({

                    "reply": (
                        "تمام image APIs ناکام ہوئیں: "
                        f"{str(exc)}"
                    ),

                    "success": False

                }), 502


        # ====================================================
        # TEXT / CODING / TESTING / DEBUGGING ROUTING
        # ====================================================

        text_messages = [

            {
                "role": "system",
                "content": _system_with_memory()
            }

        ]


        # ----------------------------------------------------
        # Previous conversation history
        # ----------------------------------------------------

        if isinstance(
            history,
            list
        ):

            for item in history:

                if not isinstance(
                    item,
                    dict
                ):
                    continue

                text = str(
                    item.get(
                        "text",
                        ""
                    )
                ).strip()

                if not text:
                    continue

                sender = item.get(
                    "sender",
                    "user"
                )

                role = (
                    "user"
                    if sender == "user"
                    else "assistant"
                )

                text_messages.append({

                    "role": role,

                    "content": text

                })


        # ----------------------------------------------------
        # Current message
        # ----------------------------------------------------

        text_messages.append({

            "role": "user",

            "content": user_message

        })


        # ----------------------------------------------------
        # MiniMax M3 = PRIMARY
        # Timeout = 20 seconds
        # ----------------------------------------------------

        try:

            answer = openrouter_request(

                OPENROUTER_PRIMARY_MODEL,

                text_messages,

                OPENROUTER_PRIMARY_TIMEOUT

            )


            _remember_message(
                "assistant",
                answer,
                project_id
            )


            return jsonify({

                "reply": answer,

                "success": True,

                "provider": "MiniMax M3"

            })


        except Exception:

            pass


        # ----------------------------------------------------
        # Nemotron 3 Ultra = SECONDARY
        # Timeout = 20 seconds
        # ----------------------------------------------------

        try:

            answer = openrouter_request(

                OPENROUTER_SECONDARY_MODEL,

                text_messages,

                OPENROUTER_SECONDARY_TIMEOUT

            )


            _remember_message(
                "assistant",
                answer,
                project_id
            )


            return jsonify({

                "reply": answer,

                "success": True,

                "provider": "Nemotron 3 Ultra"

            })


        except Exception:

            pass


        # ----------------------------------------------------
        # Gemini = FINAL TEXT FALLBACK
        # Timeout = 15 seconds
        # ----------------------------------------------------

        try:

            formatted_contents = []


            if isinstance(
                history,
                list
            ):

                for item in history:

                    if not isinstance(
                        item,
                        dict
                    ):
                        continue

                    text = str(
                        item.get(
                            "text",
                            ""
                        )
                    ).strip()

                    if not text:
                        continue

                    sender = item.get(
                        "sender",
                        "user"
                    )

                    role = (
                        "user"
                        if sender == "user"
                        else "model"
                    )

                    formatted_contents.append(

                        types.Content(

                            role=role,

                            parts=[

                                types.Part.from_text(
                                    text=text
                                )

                            ]

                        )

                    )


            formatted_contents.append(

                types.Content(

                    role="user",

                    parts=[

                        types.Part.from_text(
                            text=user_message
                        )

                    ]

                )

            )


            answer = gemini_request(
                formatted_contents
            )


            _remember_message(
                "assistant",
                answer,
                project_id
            )


            return jsonify({

                "reply": answer,

                "success": True,

                "provider": "Gemini"

            })


        except Exception as exc:

            return jsonify({

                "reply": (
                    "تمام AI APIs ناکام ہوئیں: "
                    f"{str(exc)}"
                ),

                "success": False

            }), 502


    except Exception as exc:

        return jsonify({

            "reply": f"خرابی: {str(exc)}",

            "success": False

        }), 500


# ============================================================
# APPLICATION START
# ============================================================

if __name__ == "__main__":

    print(
        "AI Assistant server starting..."
    )

    print(
        f"OpenRouter primary: "
        f"{OPENROUTER_PRIMARY_MODEL} "
        f"({OPENROUTER_PRIMARY_TIMEOUT}s)"
    )

    print(
        f"OpenRouter secondary: "
        f"{OPENROUTER_SECONDARY_MODEL} "
        f"({OPENROUTER_SECONDARY_TIMEOUT}s)"
    )

    print(
        f"OpenRouter vision: "
        f"{OPENROUTER_VISION_MODEL} "
        f"({OPENROUTER_VISION_TIMEOUT}s)"
    )

    print(
        f"Gemini: "
        f"{GEMINI_MODEL} "
        f"({GEMINI_TIMEOUT_MS / 1000.0:g}s)"
    )

    print(
        "Local memory: "
        + (
            "AVAILABLE"
            if LOCAL_MEMORY_AVAILABLE
            else "UNAVAILABLE"
        )
    )

    if LOCAL_MEMORY_ERROR:

        print(
            "Local memory warning: "
            + LOCAL_MEMORY_ERROR
        )

    print(
        "Development tools: "
        + (
            "AVAILABLE"
            if DEVELOPMENT_TOOLS_AVAILABLE
            else "UNAVAILABLE"
        )
    )

    if DEVELOPMENT_TOOLS_ERROR:

        print(
            "Development tools warning: "
            + DEVELOPMENT_TOOLS_ERROR
        )

    app.run(
        host="0.0.0.0",
        port=5000,
        debug=True
    )
