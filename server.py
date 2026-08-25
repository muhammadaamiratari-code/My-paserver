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

app = Flask(__name__)

# CORS
CORS(app)

# ============================================================
# API CONFIGURATION
# ============================================================

# ------------------------------------------------------------
# OPENROUTER
# Text:
#   Primary   = Nemotron 3.5 Lightning
#   Secondary = Nemotron 3 Ultra
#
# Image:
#   Primary   = Gemma 4 31B
#   Fallback  = Gemini
# ------------------------------------------------------------

OPENROUTER_API_KEY = os.environ.get("OPENROUTER_API_KEY", "")

OPENROUTER_PRIMARY_MODEL = os.environ.get(
    "OPENROUTER_PRIMARY_MODEL",
    "nvidia/nemotron-3.5-lightning:free"
)

OPENROUTER_SECONDARY_MODEL = os.environ.get(
    "OPENROUTER_SECONDARY_MODEL",
    "nvidia/nemotron-3-ultra-550b-a55b:free"
)

OPENROUTER_VISION_MODEL = os.environ.get(
    "OPENROUTER_VISION_MODEL",
    "google/gemma-4-31b-it:free"
)

OPENROUTER_PRIMARY_TIMEOUT = int(
    os.environ.get("OPENROUTER_PRIMARY_TIMEOUT", "6")
)

OPENROUTER_SECONDARY_TIMEOUT = int(
    os.environ.get("OPENROUTER_SECONDARY_TIMEOUT", "8")
)

OPENROUTER_VISION_TIMEOUT = int(
    os.environ.get("OPENROUTER_VISION_TIMEOUT", "6")
)

# ------------------------------------------------------------
# GEMINI
# Final fallback for text and image
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
    os.environ.get("GEMINI_TIMEOUT_MS", "10000")
)

if GEMINI_API_KEY and GEMINI_API_KEY != "YOUR_GEMINI_API_KEY_HERE":
    client = genai.Client(
        api_key=GEMINI_API_KEY,
        http_options=types.HttpOptions(
            timeout=GEMINI_TIMEOUT_MS
        )
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
# OPENROUTER REQUEST HELPER
# ============================================================

def openrouter_request(model, messages, timeout_seconds):
    if not OPENROUTER_API_KEY:
        raise RuntimeError("OpenRouter API is not configured.")

    payload = {
        "model": model,
        "messages": messages
    }

    body = json.dumps(payload).encode("utf-8")

    req = urllib.request.Request(
        "https://openrouter.ai/api/v1/chat/completions",
        data=body,
        headers={
            "Authorization": f"Bearer {OPENROUTER_API_KEY}",
            "Content-Type": "application/json",
        },
        method="POST"
    )

    with urllib.request.urlopen(
        req,
        timeout=timeout_seconds
    ) as response:
        raw = response.read().decode("utf-8")

    result = json.loads(raw)

    choices = result.get("choices", [])

    if not choices:
        raise RuntimeError("OpenRouter returned no choices.")

    message = choices[0].get("message", {})
    answer = message.get("content")

    if isinstance(answer, list):
        text_parts = []

        for part in answer:
            if isinstance(part, dict):
                text = part.get("text")
                if text:
                    text_parts.append(str(text))

        answer = "\n".join(text_parts)

    if not answer:
        raise RuntimeError("OpenRouter returned an empty response.")

    return str(answer).strip()


# ============================================================
# GEMINI REQUEST HELPER
# ============================================================

def gemini_request(contents):
    if not client:
        raise RuntimeError("Gemini API is not configured.")

    response = client.models.generate_content(
        model=GEMINI_MODEL,
        contents=contents,
        config=types.GenerateContentConfig(
            system_instruction=SYSTEM_INSTRUCTION
        )
    )

    answer = getattr(response, "text", None)

    if not answer:
        raise RuntimeError("Gemini returned an empty response.")

    return answer.strip()


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
        # ----------------------------------------------------
        # Read request
        # ----------------------------------------------------

        data = request.get_json(silent=True)

        if not isinstance(data, dict):
            data = {}

        user_message = str(
            data.get("message", "")
        ).strip()

        history = data.get("history", [])

        image_data = data.get("image", None)

        # ----------------------------------------------------
        # Empty message check
        # ----------------------------------------------------

        if not user_message:
            return jsonify({
                "reply": "Please enter a message.",
                "success": False
            }), 400

        # ====================================================
        # IMAGE / SCREENSHOT ROUTING
        # ====================================================

        if image_data:

            try:
                image_string = image_data

                if "," in image_string:
                    image_string = image_string.split(
                        ",",
                        1
                    )[1]

                image_bytes = base64.b64decode(
                    image_string
                )

            except Exception:
                return jsonify({
                    "reply": "The image could not be processed.",
                    "success": False
                }), 400

            # ------------------------------------------------
            # Convert image to data URL for OpenRouter
            # ------------------------------------------------

            image_data_url = (
                "data:image/jpeg;base64,"
                + base64.b64encode(
                    image_bytes
                ).decode("utf-8")
            )

            vision_messages = [
                {
                    "role": "system",
                    "content": SYSTEM_INSTRUCTION
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
            # ------------------------------------------------

            try:
                answer = openrouter_request(
                    OPENROUTER_VISION_MODEL,
                    vision_messages,
                    OPENROUTER_VISION_TIMEOUT
                )

                return jsonify({
                    "reply": answer,
                    "success": True,
                    "provider": "Gemma 4 31B"
                })

            except Exception:
                pass

            # ------------------------------------------------
            # Gemini = IMAGE FINAL FALLBACK
            # ------------------------------------------------

            try:
                formatted_contents = []

                if isinstance(history, list):
                    for item in history:
                        if not isinstance(item, dict):
                            continue

                        text = str(
                            item.get("text", "")
                        ).strip()

                        if not text:
                            continue

                        sender = item.get(
                            "sender",
                            "user"
                        )

                        if sender == "user":
                            formatted_contents.append(
                                types.Content(
                                    role="user",
                                    parts=[
                                        types.Part.from_text(
                                            text=text
                                        )
                                    ]
                                )
                            )
                        else:
                            formatted_contents.append(
                                types.Content(
                                    role="model",
                                    parts=[
                                        types.Part.from_text(
                                            text=text
                                        )
                                    ]
                                )
                            )

                current_parts = [
                    types.Part.from_text(
                        text=user_message
                    ),
                    types.Part.from_bytes(
                        data=image_bytes,
                        mime_type="image/jpeg"
                    )
                ]

                formatted_contents.append(
                    types.Content(
                        role="user",
                        parts=current_parts
                    )
                )

                answer = gemini_request(
                    formatted_contents
                )

                return jsonify({
                    "reply": answer,
                    "success": True,
                    "provider": "Gemini"
                })

            except Exception as e:
                return jsonify({
                    "reply": f"تمام image APIs ناکام ہوئیں: {str(e)}",
                    "success": False
                }), 502

        # ====================================================
        # TEXT / CODING / TESTING / DEBUGGING ROUTING
        # ====================================================

        text_messages = [
            {
                "role": "system",
                "content": SYSTEM_INSTRUCTION
            }
        ]

        # ----------------------------------------------------
        # Previous conversation history
        # ----------------------------------------------------

        if isinstance(history, list):
            for item in history:

                if not isinstance(item, dict):
                    continue

                text = str(
                    item.get("text", "")
                ).strip()

                if not text:
                    continue

                sender = item.get(
                    "sender",
                    "user"
                )

                if sender == "user":
                    text_messages.append({
                        "role": "user",
                        "content": text
                    })

                else:
                    text_messages.append({
                        "role": "assistant",
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
        # Nemotron 3.5 = PRIMARY
        # Timeout = 6 seconds
        # ----------------------------------------------------

        try:
            answer = openrouter_request(
                OPENROUTER_PRIMARY_MODEL,
                text_messages,
                OPENROUTER_PRIMARY_TIMEOUT
            )

            return jsonify({
                "reply": answer,
                "success": True,
                "provider": "Nemotron 3.5 Lightning"
            })

        except Exception:
            pass

        # ----------------------------------------------------
        # Nemotron 3 Ultra = SECONDARY
        # Timeout = 8 seconds
        # ----------------------------------------------------

        try:
            answer = openrouter_request(
                OPENROUTER_SECONDARY_MODEL,
                text_messages,
                OPENROUTER_SECONDARY_TIMEOUT
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
        # Timeout = 10 seconds
        # ----------------------------------------------------

        try:
            formatted_contents = []

            if isinstance(history, list):
                for item in history:

                    if not isinstance(item, dict):
                        continue

                    text = str(
                        item.get("text", "")
                    ).strip()

                    if not text:
                        continue

                    sender = item.get(
                        "sender",
                        "user"
                    )

                    if sender == "user":
                        formatted_contents.append(
                            types.Content(
                                role="user",
                                parts=[
                                    types.Part.from_text(
                                        text=text
                                    )
                                ]
                            )
                        )
                    else:
                        formatted_contents.append(
                            types.Content(
                                role="model",
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

            return jsonify({
                "reply": answer,
                "success": True,
                "provider": "Gemini"
            })

        except Exception as e:
            return jsonify({
                "reply": f"تمام AI APIs ناکام ہوئیں: {str(e)}",
                "success": False
            }), 502

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
