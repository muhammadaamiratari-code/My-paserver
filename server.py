from flask import Flask, request, jsonify
from dotenv import load_dotenv
load_dotenv("/home/MAmir353/My-paserver/.env")
from flask_cors import CORS
import os
import json
import urllib.request
import urllib.error

app = Flask(__name__)
CORS(app)

OPENAI_URL = "https://api.openai.com/v1/responses"

@app.route("/")
def home():
    return jsonify({"status": "online", "name": "MyPA"})

@app.route("/health")
def health():
    return jsonify({"ok": True, "api_key_configured": bool(os.environ.get("OPENAI_API_KEY"))})

@app.route("/chat", methods=["POST"])
def chat():
    api_key = os.environ.get("OPENAI_API_KEY")

    if not api_key:
        return jsonify({"error": "OPENAI_API_KEY is not configured"}), 500

    data = request.get_json(silent=True) or {}
    message = str(data.get("message", "")).strip()

    if not message:
        return jsonify({"error": "Message is empty"}), 400

    payload = {
        "model": "gpt-5-mini",
        "input": message
    }

    req = urllib.request.Request(
        OPENAI_URL,
        data=json.dumps(payload).encode("utf-8"),
        headers={
            "Content-Type": "application/json",
            "Authorization": "Bearer " + api_key
        },
        method="POST"
    )

    try:
        with urllib.request.urlopen(req, timeout=60) as response:
            result = json.loads(response.read().decode("utf-8"))

        reply = result.get("output_text", "").strip()

        return jsonify({
            "ok": True,
            "reply": reply
        })

    except urllib.error.HTTPError as e:
        details = e.read().decode("utf-8", errors="replace")
        return jsonify({
            "ok": False,
            "error": "OpenAI API error",
            "details": details
        }), e.code

    except Exception as e:
        return jsonify({
            "ok": False,
            "error": str(e)
        }), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=False)
