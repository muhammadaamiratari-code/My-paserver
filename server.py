import os
import json
import urllib.request
import urllib.error
from flask import Flask, request, jsonify
from flask_cors import CORS
from dotenv import load_dotenv

# .env فائل کو لوڈ کرنا
load_dotenv("/home/MAmir353/My-paserver/.env")

app = Flask(__name__)
CORS(app)

GEMINI_MODEL = "gemini-1.5-flash"

def call_gemini_api(message, api_key):
    url = f"https://generativelanguage.googleapis.com/v1beta/models/{GEMINI_MODEL}:generateContent?key={api_key}"
    payload = {
        "contents": [
            {
                "parts": [{"text": message}]
            }
        ]
    }
    req = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST"
    )
    with urllib.request.urlopen(req, timeout=30) as response:
        result = json.loads(response.read().decode("utf-8"))
        reply = result["candidates"][0]["content"]["parts"][0]["text"].strip()
        return reply

def call_openai_api(message, api_key):
    url = "https://api.openai.com/v1/chat/completions"
    payload = {
        "model": "gpt-4o-mini",
        "messages": [
            {"role": "user", "content": message}
        ]
    }
    req = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8"),
        headers={
            "Content-Type": "application/json",
            "Authorization": f"Bearer {api_key}"
        },
        method="POST"
    )
    with urllib.request.urlopen(req, timeout=30) as response:
        result = json.loads(response.read().decode("utf-8"))
        reply = result["choices"][0]["message"]["content"].strip()
        return reply

@app.route("/")
def home():
    return jsonify({"status": "online", "name": "MyPA"})

@app.route("/health")
def health():
    gemini_key = os.environ.get("GEMINI_API_KEY")
    openai_key = os.environ.get("OPENAI_API_KEY")
    return jsonify({
        "ok": True,
        "gemini_configured": bool(gemini_key),
        "openai_configured": bool(openai_key)
    })

@app.route("/chat", methods=["POST"])
def chat():
    data = request.get_json(silent=True) or {}
    message = str(data.get("message", "")).strip()

    if not message:
        return jsonify({
            "ok": False,
            "error": "Message is empty. Please enter a message."
        }), 400

    gemini_key = os.environ.get("GEMINI_API_KEY")
    openai_key = os.environ.get("OPENAI_API_KEY")

    # 1. First try Gemini API
    if gemini_key:
        try:
            reply = call_gemini_api(message, gemini_key)
            if reply:
                return jsonify({
                    "ok": True,
                    "reply": reply,
                    "source": "gemini"
                })
        except Exception as e:
            print(f"Gemini API error: {e}")

    # 2. Fallback to OpenAI API
    if openai_key:
        try:
            reply = call_openai_api(message, openai_key)
            if reply:
                return jsonify({
                    "ok": True,
                    "reply": reply,
                    "source": "openai"
                })
        except Exception as e:
            print(f"OpenAI API error: {e}")

    # 3. Friendly English fallback error if both fail
    return jsonify({
        "ok": False,
        "error": "Our AI assistant is temporarily busy processing requests. Please wait a moment and try again!"
    }), 503

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=False)
                    
