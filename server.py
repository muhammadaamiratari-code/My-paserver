import os
import base64
import json
import urllib.request

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
    dev_tools = backup_manager = testing_manager = browser_testing = git_manager = None
    DEVELOPMENT_TOOLS_AVAILABLE = False
    DEVELOPMENT_TOOLS_ERROR = str(exc)


app = Flask(__name__)
CORS(app)


# ============================================================
# API CONFIGURATION
# ============================================================
OPENROUTER_API_KEY = os.environ.get("OPENROUTER_API_KEY", "")
OPENROUTER_PRIMARY_MODEL = os.environ.get("OPENROUTER_PRIMARY_MODEL", "minimax/minimax-m3:free")
OPENROUTER_SECONDARY_MODEL = os.environ.get("OPENROUTER_SECONDARY_MODEL", "nvidia/nemotron-3-ultra-550b-a55b:free")
OPENROUTER_VISION_MODEL = os.environ.get("OPENROUTER_VISION_MODEL", "google/gemma-4-31b-it:free")
OPENROUTER_PRIMARY_TIMEOUT = int(os.environ.get("OPENROUTER_PRIMARY_TIMEOUT", "20"))
OPENROUTER_SECONDARY_TIMEOUT = int(os.environ.get("OPENROUTER_SECONDARY_TIMEOUT", "20"))
OPENROUTER_VISION_TIMEOUT = int(os.environ.get("OPENROUTER_VISION_TIMEOUT", "20"))

GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY", "YOUR_GEMINI_API_KEY_HERE")
GEMINI_MODEL = os.environ.get("GEMINI_MODEL", "gemini-3.6-flash")
GEMINI_TIMEOUT_MS = int(os.environ.get("GEMINI_TIMEOUT_MS", "15000"))

client = None
if GEMINI_API_KEY and GEMINI_API_KEY != "YOUR_GEMINI_API_KEY_HERE":
    try:
        client = genai.Client(
            api_key=GEMINI_API_KEY,
            http_options=types.HttpOptions(timeout=float(GEMINI_TIMEOUT_MS / 1000.0))
        )
    except Exception:
        client = None


SYSTEM_INSTRUCTION = (
    TRUTH_POLICY + "\n\n" +
    PROFESSIONAL_DEVELOPER_POLICY + "\n\n" +
    PROJECT_MEMORY_POLICY + "\n\n" +
    WORKFLOW_POLICY + "\n\n" +
    SECURITY_POLICY + "\n\n" +
    GENERAL_CONVERSATION_POLICY + "\n\n" +
    FACT_CHECKING_LIMITATION + "\n\n" +
    USER_PERSONAL_INSTRUCTIONS
)


# ============================================================
# LOCAL MEMORY HELPERS
# ============================================================
def _memory_project_id():
    if not LOCAL_MEMORY_AVAILABLE:
        return None
    try:
        project = local_memory.get_active_project()
        return project["id"] if project else None
    except Exception:
        return None


def _memory_context():
    if not LOCAL_MEMORY_AVAILABLE:
        return ""
    try:
        project = local_memory.get_active_project()
        if not project:
            return ""
        context = local_memory.get_project_context(project["id"])
        if not context:
            return ""
        return json.dumps(context, ensure_ascii=False, default=str)[:30000]
    except Exception:
        return ""


def _system_with_memory():
    context = _memory_context()
    if not context:
        return SYSTEM_INSTRUCTION
    return SYSTEM_INSTRUCTION + "\n\n=== LOCAL DEVELOPER MEMORY CONTEXT ===\n" + context + "\n=== END LOCAL DEVELOPER MEMORY CONTEXT ==="


def _remember_message(role, content, project_id=None):
    if not LOCAL_MEMORY_AVAILABLE:
        return False
    try:
        pid = project_id if project_id is not None else _memory_project_id()
        if pid is None:
            return False
        local_memory.add_chat_message(pid, role, str(content))
        return True
    except Exception:
        return False


def _remember_change(summary, file_path="server.py", result=""):
    if not LOCAL_MEMORY_AVAILABLE:
        return False
    try:
        pid = _memory_project_id()
        if pid is None:
            return False
        local_memory.record_change(pid, summary=summary, file_path=file_path, reason="Server/API routing or memory integration", result=result)
        return True
    except Exception:
        return False


# ============================================================
# DEVELOPMENT / TESTING TOOL BRIDGE
# ============================================================
def _development_tools_ready():
    return DEVELOPMENT_TOOLS_AVAILABLE and all(
        module is not None
        for module in (dev_tools, backup_manager, testing_manager, browser_testing, git_manager)
    )


def _tool_result(module, function_name, *args, **kwargs):
    if not _development_tools_ready():
        return False, "Development/testing tools unavailable: " + DEVELOPMENT_TOOLS_ERROR
    try:
        function = getattr(module, function_name)
    except AttributeError:
        return False, f"Tool function not available: {function_name}"
    try:
        return function(*args, **kwargs)
    except Exception as exc:
        return False, f"{function_name} میں مسئلہ: {exc}"


TOOL_ACTIONS = {
    "dev_tools": {"read_file", "find_errors", "edit_file", "add_code", "list_project_files"},
    "backup_manager": {"create_backup", "restore_backup", "list_backups"},
    "testing_manager": {"run_python_syntax", "run_python_file", "check_server_health", "get_test_history"},
    "browser_testing": {"open_browser", "check_page_status", "test_form"},
    "git_manager": {"git_status", "git_diff", "git_add", "git_commit", "git_push", "git_pull", "git_log", "git_create_branch"},
}

DESTRUCTIVE_ACTIONS = {
    ("dev_tools", "edit_file"),
    ("dev_tools", "add_code"),
    ("backup_manager", "restore_backup"),
    ("git_manager", "git_add"),
    ("git_manager", "git_commit"),
    ("git_manager", "git_push"),
    ("git_manager", "git_pull"),
    ("git_manager", "git_create_branch"),
}


def _destructive_request_allowed(user_message, tool, action):
    if (tool, action) not in DESTRUCTIVE_ACTIONS:
        return True
    text = str(user_message).lower()
    keywords = {
        "edit_file": ("edit", "modify", "change", "fix", "update", "تبدیل", "تبدیلی", "ترمیم", "درست", "فکس", "تبدیل کریں", "درست کریں"),
        "add_code": ("add code", "code add", "include code", "کوڈ شامل", "کوڈ ایڈ", "شامل کریں"),
        "restore_backup": ("restore", "بحال", "ری اسٹور"),
        "git_add": ("git add", "add to git", "git میں add"),
        "git_commit": ("commit", "کمٹ"),
        "git_push": ("push", "git push", "پش"),
        "git_pull": ("pull", "git pull", "پل"),
        "git_create_branch": ("branch", "برانچ"),
    }
    return any(word in text for word in keywords.get(action, ()))


def _execute_development_tool(tool, action, arguments, user_message):
    if tool not in TOOL_ACTIONS or action not in TOOL_ACTIONS[tool]:
        return False, f"Unsupported development tool action: {tool}/{action}"
    if not isinstance(arguments, dict):
        return False, "Tool arguments dictionary ہونا چاہیے۔"
    if not _destructive_request_allowed(user_message, tool, action):
        return False, "یہ تبدیلی والا tool user کی واضح درخواست کے بغیر نہیں چلایا جا سکتا۔"

    if tool == "dev_tools":
        mapping = {
            "read_file": ("read_file", (arguments.get("file_path",),)),
            "find_errors": ("find_errors", (arguments.get("file_path",),)),
            "edit_file": ("edit_file", (arguments.get("file_path", ""), arguments.get("old_text", ""), arguments.get("new_text", ""), arguments.get("create_backup", True))),
            "add_code": ("add_code", (arguments.get("file_path", ""), arguments.get("new_code", ""), arguments.get("position", "end"), arguments.get("create_backup", True))),
            "list_project_files": ("list_project_files", (arguments.get("extensions"),)),
        }
        fn, args = mapping[action]
        return _tool_result(dev_tools, fn, *args)

    if tool == "backup_manager":
        mapping = {
            "create_backup": ("create_backup", (arguments.get("file_path", ""), arguments.get("reason", ""))),
            "restore_backup": ("restore_backup", (arguments.get("backup_path", ""),)),
            "list_backups": ("list_backups", (arguments.get("file_path"),)),
        }
        fn, args = mapping[action]
        return _tool_result(backup_manager, fn, *args)

    if tool == "testing_manager":
        mapping = {
            "run_python_syntax": ("run_python_syntax", (arguments.get("file_path", ""),)),
            "run_python_file": ("run_python_file", (arguments.get("file_path", ""), arguments.get("args"))),
            "check_server_health": ("check_server_health", (arguments.get("url", "http://127.0.0.1:5000/api/health"),)),
            "get_test_history": ("get_test_history", (arguments.get("test_type"), arguments.get("target"))),
        }
        fn, args = mapping[action]
        return _tool_result(testing_manager, fn, *args)

    if tool == "browser_testing":
        mapping = {
            "open_browser": ("open_browser", (arguments.get("url", ""),)),
            "check_page_status": ("check_page_status", (arguments.get("url", ""), arguments.get("expected_status", 200))),
            "test_form": ("test_form", (arguments.get("url", ""), arguments.get("form_data", {}), arguments.get("method", "POST"))),
        }
        fn, args = mapping[action]
        return _tool_result(browser_testing, fn, *args)

    mapping = {
        "git_status": ("git_status", (arguments.get("repo_path"),)),
        "git_diff": ("git_diff", (arguments.get("file_path"), arguments.get("repo_path"))),
        "git_add": ("git_add", (arguments.get("file_path", ""), arguments.get("repo_path"))),
        "git_commit": ("git_commit", (arguments.get("message", ""), arguments.get("repo_path"))),
        "git_push": ("git_push", (arguments.get("branch", "main"), arguments.get("repo_path"))),
        "git_pull": ("git_pull", (arguments.get("branch", "main"), arguments.get("repo_path"))),
        "git_log": ("git_log", (arguments.get("limit", 10), arguments.get("repo_path"))),
        "git_create_branch": ("git_create_branch", (arguments.get("branch_name", ""), arguments.get("repo_path"))),
    }
    fn, args = mapping[action]
    return _tool_result(git_manager, fn, *args)


DEVELOPMENT_TOOL_DEFINITION = {
    "type": "function",
    "function": {
        "name": "development_tool",
        "description": "Use the project's existing development/testing tools. Read, inspect, test, backup, browser-test, or perform Git operations only when appropriate. Do not invent tool results.",
        "parameters": {
            "type": "object",
            "properties": {
                "tool": {"type": "string", "enum": list(TOOL_ACTIONS.keys())},
                "action": {"type": "string"},
                "arguments": {"type": "object"}
            },
            "required": ["tool", "action", "arguments"],
            "additionalProperties": False
        }
    }
}


def _messages_from_history(history, current_message):
    messages = [{"role": "system", "content": _system_with_memory()}]
    if isinstance(history, list):
        for item in history:
            if not isinstance(item, dict):
                continue
            text = str(item.get("text", "")).strip()
            if not text:
                continue
            sender = item.get("sender", "user")
            messages.append({"role": "user" if sender == "user" else "assistant", "content": text})
    messages.append({"role": "user", "content": current_message})
    return messages


def _openrouter_raw(model, messages, timeout_seconds, tools=None):
    if not OPENROUTER_API_KEY:
        raise RuntimeError("OpenRouter API is not configured.")
    payload = {"model": model, "messages": messages}
    if tools:
        payload["tools"] = tools
        payload["tool_choice"] = "auto"
    body = json.dumps(payload).encode("utf-8")
    req = urllib.request.Request(
        "https://openrouter.ai/api/v1/chat/completions",
        data=body,
        headers={"Authorization": f"Bearer {OPENROUTER_API_KEY}", "Content-Type": "application/json"},
        method="POST"
    )
    with urllib.request.urlopen(req, timeout=timeout_seconds) as response:
        raw = response.read().decode("utf-8")
    result = json.loads(raw)
    choices = result.get("choices", [])
    if not choices:
        raise RuntimeError("OpenRouter returned no choices.")
    return choices[0].get("message", {})


def _answer_from_message(message):
    answer = message.get("content")
    if isinstance(answer, list):
        answer = "\n".join(str(part.get("text")) for part in answer if isinstance(part, dict) and part.get("text"))
    if not answer:
        raise RuntimeError("OpenRouter returned an empty response.")
    return str(answer).strip()


def openrouter_request(model, messages, timeout_seconds):
    return _answer_from_message(_openrouter_raw(model, messages, timeout_seconds))


def openrouter_request_with_tools(model, messages, timeout_seconds, user_message, max_rounds=3):
    working = list(messages)
    for _ in range(max_rounds):
        message = _openrouter_raw(model, working, timeout_seconds, tools=[DEVELOPMENT_TOOL_DEFINITION])
        tool_calls = message.get("tool_calls") or []
        if not tool_calls:
            return _answer_from_message(message)
        working.append(message)
        for call in tool_calls:
            function = call.get("function", {})
            name = function.get("name")
            if name != "development_tool":
                result = (False, "Unknown tool function.")
            else:
                try:
                    arguments = json.loads(function.get("arguments", "{}"))
                except json.JSONDecodeError:
                    arguments = {}
                    result = (False, "Tool arguments JSON درست نہیں ہے۔")
                else:
                    result = _execute_development_tool(
                        arguments.get("tool", ""),
                        arguments.get("action", ""),
                        arguments.get("arguments", {}),
                        user_message
                    )
            working.append({
                "role": "tool",
                "tool_call_id": call.get("id", ""),
                "content": json.dumps({"success": bool(result[0]), "result": result[1]}, ensure_ascii=False, default=str)
            })
    return openrouter_request(model, working, timeout_seconds)


# ============================================================
# GEMINI REQUEST HELPER
# ============================================================
def gemini_request(contents):
    if not client:
        raise RuntimeError("Gemini API is not configured.")
    response = client.models.generate_content(
        model=GEMINI_MODEL,
        contents=contents,
        config=types.GenerateContentConfig(system_instruction=_system_with_memory())
    )
    answer = getattr(response, "text", None)
    if not answer:
        raise RuntimeError("Gemini returned an empty response.")
    return answer.strip()


@app.route("/")
def index():
    return render_template("index.html")


@app.route("/api/health", methods=["GET"])
def health():
    return jsonify({
        "assistant": "AI Assistant", "service": "AI Assistant", "status": "OK",
        "openrouter_configured": bool(OPENROUTER_API_KEY), "gemini_configured": bool(client),
        "local_memory_available": LOCAL_MEMORY_AVAILABLE,
        "development_tools_available": DEVELOPMENT_TOOLS_AVAILABLE,
        "development_tools_error": DEVELOPMENT_TOOLS_ERROR,
        "primary_model": OPENROUTER_PRIMARY_MODEL, "secondary_model": OPENROUTER_SECONDARY_MODEL,
        "vision_model": OPENROUTER_VISION_MODEL,
        "primary_timeout": OPENROUTER_PRIMARY_TIMEOUT, "secondary_timeout": OPENROUTER_SECONDARY_TIMEOUT,
        "vision_timeout": OPENROUTER_VISION_TIMEOUT, "gemini_model": GEMINI_MODEL,
        "gemini_timeout_seconds": GEMINI_TIMEOUT_MS / 1000.0,
    })


@app.route("/api/dev-tools", methods=["POST"])
def development_tools():
    data = request.get_json(silent=True)
    if not isinstance(data, dict):
        data = {}
    tool = str(data.get("tool", "")).strip()
    action = str(data.get("action", "")).strip()
    if not tool or not action:
        return jsonify({"success": False, "reply": "Development tool اور action ضروری ہیں۔"}), 400
    success, result = _execute_development_tool(tool, action, data.get("arguments", {}), str(data.get("user_message", "")))
    return jsonify({"success": bool(success), "tool": tool, "action": action, "result": result}), (200 if success else 400)


@app.route("/api/chat", methods=["POST"])
def chat():
    project_id = _memory_project_id()
    try:
        data = request.get_json(silent=True)
        if not isinstance(data, dict):
            data = {}
        user_message = str(data.get("message", "")).strip()
        history = data.get("history", [])
        image_data = data.get("image")
        if not user_message:
            return jsonify({"reply": "Please enter a message.", "success": False}), 400
        _remember_message("user", user_message, project_id)

        if image_data:
            try:
                image_string = image_data
                mime_type = "image/jpeg"
                if "," in image_string:
                    header, image_string = image_string.split(",", 1)
                    if "data:" in header and ";base64" in header:
                        mime_type = header.split(";")[0].replace("data:", "")
                image_bytes = base64.b64decode(image_string)
            except Exception:
                return jsonify({"reply": "The image could not be processed.", "success": False}), 400

            image_data_url = f"data:{mime_type};base64," + base64.b64encode(image_bytes).decode("utf-8")
            vision_messages = [
                {"role": "system", "content": _system_with_memory()},
                {"role": "user", "content": [
                    {"type": "text", "text": user_message},
                    {"type": "image_url", "image_url": {"url": image_data_url}}
                ]}
            ]
            try:
                answer = openrouter_request(OPENROUTER_VISION_MODEL, vision_messages, OPENROUTER_VISION_TIMEOUT)
                _remember_message("assistant", answer, project_id)
                return jsonify({"reply": answer, "success": True, "provider": "Gemma 4 31B"})
            except Exception:
                pass

            try:
                formatted_contents = []
                if isinstance(history, list):
                    for item in history:
                        if not isinstance(item, dict):
                            continue
                        text = str(item.get("text", "")).strip()
                        if not text:
                            continue
                        role = "user" if item.get("sender", "user") == "user" else "model"
                        formatted_contents.append(types.Content(role=role, parts=[types.Part.from_text(text=text)]))
                formatted_contents.append(types.Content(role="user", parts=[types.Part.from_text(text=user_message), types.Part.from_bytes(data=image_bytes, mime_type=mime_type)]))
                answer = gemini_request(formatted_contents)
                _remember_message("assistant", answer, project_id)
                return jsonify({"reply": answer, "success": True, "provider": "Gemini"})
            except Exception as exc:
                return jsonify({"reply": "تمام image APIs ناکام ہوئیں: " + str(exc), "success": False}), 502

        text_messages = _messages_from_history(history, user_message)
        try:
            answer = openrouter_request_with_tools(OPENROUTER_PRIMARY_MODEL, text_messages, OPENROUTER_PRIMARY_TIMEOUT, user_message)
            _remember_message("assistant", answer, project_id)
            return jsonify({"reply": answer, "success": True, "provider": "MiniMax M3"})
        except Exception:
            pass

        try:
            answer = openrouter_request_with_tools(OPENROUTER_SECONDARY_MODEL, text_messages, OPENROUTER_SECONDARY_TIMEOUT, user_message)
            _remember_message("assistant", answer, project_id)
            return jsonify({"reply": answer, "success": True, "provider": "Nemotron 3 Ultra"})
        except Exception:
            pass

        try:
            formatted_contents = []
            if isinstance(history, list):
                for item in history:
                    if not isinstance(item, dict):
                        continue
                    text = str(item.get("text", "")).strip()
                    if not text:
                        continue
                    role = "user" if item.get("sender", "user") == "user" else "model"
                    formatted_contents.append(types.Content(role=role, parts=[types.Part.from_text(text=text)]))
            formatted_contents.append(types.Content(role="user", parts=[types.Part.from_text(text=user_message)]))
            answer = gemini_request(formatted_contents)
            _remember_message("assistant", answer, project_id)
            return jsonify({"reply": answer, "success": True, "provider": "Gemini"})
        except Exception as exc:
            return jsonify({"reply": "تمام AI APIs ناکام ہوئیں: " + str(exc), "success": False}), 502

    except Exception as exc:
        return jsonify({"reply": f"خرابی: {str(exc)}", "success": False}), 500


if __name__ == "__main__":
    print("AI Assistant server starting...")
    print(f"OpenRouter primary: {OPENROUTER_PRIMARY_MODEL} ({OPENROUTER_PRIMARY_TIMEOUT}s)")
    print(f"OpenRouter secondary: {OPENROUTER_SECONDARY_MODEL} ({OPENROUTER_SECONDARY_TIMEOUT}s)")
    print(f"OpenRouter vision: {OPENROUTER_VISION_MODEL} ({OPENROUTER_VISION_TIMEOUT}s)")
    print(f"Gemini: {GEMINI_MODEL} ({GEMINI_TIMEOUT_MS / 1000.0:g}s)")
    print("Local memory: " + ("AVAILABLE" if LOCAL_MEMORY_AVAILABLE else "UNAVAILABLE"))
    if LOCAL_MEMORY_ERROR:
        print("Local memory warning: " + LOCAL_MEMORY_ERROR)
    print("Development tools: " + ("AVAILABLE" if DEVELOPMENT_TOOLS_AVAILABLE else "UNAVAILABLE"))
    if DEVELOPMENT_TOOLS_ERROR:
        print("Development tools warning: " + DEVELOPMENT_TOOLS_ERROR)
    app.run(host="0.0.0.0", port=5000, debug=True)
