import os
import base64
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

app = Flask(__name__)

# CORS
CORS(app)

# ============================================================
# GEMINI API CONFIGURATION
# ============================================================

GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY", "YOUR_GEMINI_API_KEY_HERE")

GEMINI_MODEL = os.environ.get(
    "GEMINI_MODEL",
    "gemini-1.5-flash"
)

if GEMINI_API_KEY:
    client = genai.Client(
        api_key=GEMINI_API_KEY
    )
else:
    client = None

# ============================================================
# SYSTEM INSTRUCTION
# Truth Policy + Professional Development + Project Memory +
# Workflow + Security + General Conversation +
# Fact-Checking Limitation + Personal Instructions
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
# HOME
# ============================================================

@app.route("/")
def index():
    return render_template("index.html")

# ============================================================
# CHAT API
# ============================================================

@app.route("/api/chat", methods=["POST"])
def chat():
    try:
        # Check Gemini configuration
        if not client:
            return jsonify({
                "reply": "Gemini API is not configured.",
                "success": False
            }), 500

        # Read request
        data = request.get_json(silent=True)
        if not isinstance(data, dict):
            data = {}

        user_message = str(data.get("message", "")).strip()
        history = data.get("history", [])
        image_data = data.get("image", None)

        # Empty message check
        if not user_message:
            return jsonify({
                "reply": "Please enter a message.",
                "success": False
            }), 400

        # Conversation contents
        formatted_contents = []

        # Previous conversation history
        if isinstance(history, list):
            for item in history:
                if not isinstance(item, dict):
                    continue

                text = str(item.get("text", "")).strip()
                if not text:
                    continue

                sender = item.get("sender", "user")

                if sender == "user":
                    formatted_contents.append(
                        types.Content(
                            role="user",
                            parts=[types.Part.from_text(text=text)]
                        )
                    )
                else:
                    formatted_contents.append(
                        types.Content(
                            role="model",
                            parts=[types.Part.from_text(text=text)]
                        )
                    )

        # Current message
        current_parts = [
            types.Part.from_text(text=user_message)
        ]

        # Image support
        if image_data:
            try:
                image_string = image_data

                if "," in image_string:
                    image_string = image_string.split(",", 1)[1]

                image_bytes = base64.b64decode(image_string)

                current_parts.append(
                    types.Part.from_bytes(
                        data=image_bytes,
                        mime_type="image/jpeg"
                    )
                )

            except Exception:
                return jsonify({
                    "reply": "The image could not be processed.",
                    "success": False
                }), 400

        formatted_contents.append(
            types.Content(
                role="user",
                parts=current_parts
            )
        )

        # Gemini request with Truth Policy
        response = client.models.generate_content(
            model=GEMINI_MODEL,
            contents=formatted_contents,
            config=types.GenerateContentConfig(
                system_instruction=SYSTEM_INSTRUCTION
            )
        )

        # Response text
        answer = getattr(response, "text", None)

        if not answer:
            return jsonify({
                "reply": "Gemini returned an empty response.",
                "success": False
            }), 502

        return jsonify({
            "reply": answer.strip(),
            "success": True,
            "provider": "Gemini"
        })

    except Exception as e:
        return jsonify({
            "reply": f"خرابی: {str(e)}",
            "success": False
        }), 500

# ============================================================
# APPLICATION START
# ============================================================

if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=5000,
        debug=True
    )
