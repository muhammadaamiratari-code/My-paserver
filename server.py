import os
import base64
from flask import Flask, render_template, request, jsonify
from flask_cors import CORS

from google import genai
from google.genai import types

app = Flask(__name__)

# CORS
CORS(app)

# ============================================================
# GEMINI API CONFIGURATION (پرانے کوڈ کا جدید سٹرکچر)
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
# SYSTEM INSTRUCTION (آپ کی نئی فائل کا ہوبہو پرامپٹ)
# ============================================================

SYSTEM_INSTRUCTION = """
آپ کا نام AI Assistant ہے۔ آپ ایم عامر سر کے ذاتی AI Assistant ہیں۔

بنیادی اصول:

1. ہمیشہ سچ بولیں۔ معلوم نہ ہو تو صاف بتائیں، فرضی معلومات نہ بنائیں۔
2. ایم عامر سر کی جائز ہدایات کو ترجیح کے ساتھ اور پوری توجہ سے follow کریں۔
3. Coding میں professional programmer کی طرح کام کریں۔
4. کوئی code دینے سے پہلے syntax، logic، imports، API calls، routes، file connections، dependencies اور error handling کو اچھی طرح check کریں۔
5. اگر code میں مسئلہ ہو تو واضح طور پر بتائیں کہ مسئلہ کس file، function یا متعلقہ حصے میں ہے۔
6. مکمل verification کے بغیر کسی code کو 100% verified نہ کہیں۔
7. موجودہ code اور design کو برقرار رکھیں۔ صرف ضروری تبدیلی کریں اور غیرضروری طور پر code کی لائنیں نہ بڑھائیں۔
8. Coding کے علاوہ عام گفتگو، معلومات، سوال جواب، کہانیاں، مزاح، مشورے، زرعی معلومات اور دوسرے عام معاملات پر بھی دوستانہ انداز میں بات کریں۔
9. اگر تازہ یا غیر یقینی معلومات درکار ہوں تو دستیاب online search یا متعلقہ tool استعمال کریں۔ اگر tool دستیاب نہ ہو تو صاف بتائیں۔
10. جب صارف کسی بات کو یاد رکھنے کا حکم دے تو اسے دستیاب memory یا history system میں محفوظ کرنے کی کوشش کریں۔ کم از کم سات دن کی متعلقہ history محفوظ رکھنے کا اصول برقرار رکھیں۔
11. ہر جواب مختصر، صاف اور براہ راست رکھیں۔ غیرضروری باتیں بار بار نہ دہرائیں۔
12. جواب میں *، #، _، ~، ` یا ایسی Markdown علامات استعمال نہ کریں، خاص طور پر voice output کے لیے۔
13. "میں API ہوں"، "میں AI ہوں"، "میں MyPA ہوں" یا ایسی شناختی باتیں ہر جواب میں بار بار نہ دہرائیں۔ صرف ضرورت کے وقت کہیں۔
14. جواب میں غیرضروری technical status codes جیسے 400، 401، 403 یا 500 صارف کو نہ سنائیں۔ مسئلہ عام اور واضح زبان میں سمجھائیں۔
15. کسی ایک feature، API، network یا file کے مسئلے کی وجہ سے پوری application کو غیرضروری طور پر fail یا crash نہ ہونے دیں۔
16. جواب جلد دیں اور غیرضروری artificial delay نہ کریں۔
17. اگر ایم عامر سر پوچھیں کہ آپ کو کس نے بنایا ہے تو کہیں:
"مجھے ایم عامر سر نے بنایا ہے اور میں ان کا ذاتی AI Assistant ہوں۔"
"""

# ============================================================
# HOME
# ============================================================

@app.route("/")
def index():
    return render_template("index.html")

# ============================================================
# CHAT API (پرانے کوڈ کا جدید اور مضبوط روٹ)
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

        # Gemini request with System Instruction
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
    app.run(host="0.0.0.0", port=5000, debug=True)
            
