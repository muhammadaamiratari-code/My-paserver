import os
from flask import Flask, render_template, request, jsonify
import google.generativeai as genai

app = Flask(__name__)

# یہاں اپنی Gemini API Key درج کریں
GEMINI_API_KEY = os.environ.get("GEMINI_API_KEY", "YOUR_GEMINI_API_KEY_HERE")
genai.configure(api_key=GEMINI_API_KEY)

# سسٹم پرامپٹ: سچائی، اخلاص اور کوڈنگ میں مہارت
SYSTEM_INSTRUCTION = """
آپ کا نام MyPA ہے۔ آپ ایک انتہائی مخلص، وفادار اور 100% سچے پرسنل اسسٹنٹ ہیں۔
آپ کی بنیادی خصوصیات:
1. آپ ہر حال میں سچ بولیں گے۔ کبھی کوئی جھوٹ، فرضی معلومات یا دھوکہ نہیں دیں گے۔
2. آپ کے پاس پچھلی تمام بات چیت اور کوڈنگ کی یادداشت موجود ہے۔ اگر صارف پچھلے ایک ہفتے میں سے کسی خاص دن یا وقت کے کوڈ کا حوالہ دے، تو اسے درست کوڈ نکال کر دیں۔
3. آپ ایک ماہر پروگرامر اور کوڈر ہیں۔
4. اگر آپ کو کسی سوال کا جواب معلوم نہ ہو تو صاف اور سچائی سے اعتراف کریں۔
"""

@app.route("/")
def index():
    return render_template("index.html")

@app.route("/api/chat", methods=["POST"])
def chat():
    try:
        data = request.json
        user_message = data.get("message", "")
        history = data.get("history", [])
        image_data = data.get("image", None)

        # Gemini Model Setup
        model = genai.GenerativeModel("gemini-1.5-flash")

        # گفتگو کی ہسٹری مرتب کریں
        formatted_contents = [{"role": "user", "parts": [SYSTEM_INSTRUCTION]}]
        
        for item in history:
            role = "user" if item.get("sender") == "user" else "model"
            formatted_contents.append({
                "role": role,
                "parts": [item.get("text", "")]
            })

        # موجودہ میسج کی تیاری
        current_parts = [user_message]
        
        if image_data:
            # اگر تصویر بھیجی گئی ہو
            import base64
            image_bytes = base64.b64decode(image_data.split(",")[1])
            image_part = {
                "mime_type": "image/jpeg",
                "data": image_bytes
            }
            current_parts.append(image_part)

        formatted_contents.append({"role": "user", "parts": current_parts})

        response = model.generate_content(formatted_contents)
        return jsonify({"reply": response.text, "success": True})

    except Exception as e:
        return jsonify({"reply": f"خرابی: {str(e)}", "success": False})

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
