let history = JSON.parse(localStorage.getItem("mypa_chat_history")) || [];
let isRecording = false;
let recognition = null;
let currentBase64Image = null;

window.onload = () => {
    cleanOldHistory();
    renderHistory();
    initSpeechRecognition();
};

// 7 دن پرانی ہسٹری صاف کریں
function cleanOldHistory() {
    const oneWeekInMs = 7 * 24 * 60 * 60 * 1000;
    const now = Date.now();
    history = history.filter(item => (now - item.timestamp) < oneWeekInMs);
    localStorage.setItem("mypa_chat_history", JSON.stringify(history));
}

// چاٹ ہسٹری دکھانا
function renderHistory() {
    const chatContainer = document.getElementById("chat-container");
    chatContainer.innerHTML = "";
    history.forEach(item => {
        appendMessageUI(item.sender, item.text, item.image, false);
    });
}

// میسج اسکرین پر شامل کریں
function appendMessageUI(sender, text, imageSrc = null, save = true) {
    const chatContainer = document.getElementById("chat-container");
    const msgDiv = document.createElement("div");
    msgDiv.classList.add("message", sender === "user" ? "user-message" : "bot-message");

    if (imageSrc) {
        const img = document.createElement("img");
        img.src = imageSrc;
        img.classList.add("chat-image");
        msgDiv.appendChild(img);
    }

    if (text) {
        const textDiv = document.createElement("div");
        textDiv.innerHTML = formatCodeText(text);
        msgDiv.appendChild(textDiv);
    }

    // اگر جواب AI کا ہے تو ساتھ سپیکر رکھیں
    if (sender === "bot" && text) {
        const speakBtn = document.createElement("button");
        speakBtn.classList.add("speaker-btn");
        speakBtn.innerText = "🔊";
        speakBtn.onclick = () => speakText(text);
        msgDiv.appendChild(speakBtn);
    }

    chatContainer.appendChild(msgDiv);
    chatContainer.scrollTop = chatContainer.scrollHeight;

    if (save) {
        history.push({
            sender: sender,
            text: text,
            image: imageSrc,
            timestamp: Date.now()
        });
        localStorage.setItem("mypa_chat_history", JSON.stringify(history));
    }
}

// کوڈ بلاکس کی فارمیٹنگ
function formatCodeText(text) {
    return text.replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>');
}

// پیغام سرور کو بھیجیں
async function sendPayload() {
    const inputEl = document.getElementById("user-input");
    const text = inputEl.value.trim();
    const imageToSend = currentBase64Image;

    if (!text && !imageToSend) return;

    // UI صاف کریں
    inputEl.value = "";
    const tempImage = currentBase64Image;
    currentBase64Image = null;

    appendMessageUI("user", text, tempImage, true);

    try {
        const response = await fetch("/api/chat", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                message: text,
                image: tempImage,
                history: history
            })
        });

        const data = await response.json();
        if (data.success) {
            appendMessageUI("bot", data.reply, null, true);
        } else {
            appendMessageUI("bot", "خطا: " + data.reply, null, false);
        }
    } catch (err) {
        appendMessageUI("bot", "سرور سے رابطہ قائم نہیں ہو سکا۔", null, false);
    }
}

function checkEnter(e) {
    if (e.key === "Enter") {
        sendPayload();
    }
}

// پلس / ضرب بٹن کی لاجک
function handlePlusClick() {
    const plusBtn = document.getElementById("plus-btn");
    
    if (isRecording) {
        // وائس ریکارڈنگ روکیں اور بٹن واپس پلس بنا دیں
        stopSpeech();
    } else {
        // امیج اپلوڈ فائل ڈائیلاگ کھولیں
        document.getElementById("image-input").click();
    }
}

// صرف تصویر اپلوڈ کی ہینڈلنگ
function handleImageSelected(e) {
    const file = e.target.files[0];
    if (file && file.type.startsWith("image/")) {
        const reader = new FileReader();
        reader.onload = function(event) {
            currentBase64Image = event.target.result;
            sendPayload();
        };
        reader.readAsDataURL(file);
    } else {
        alert("صرف تصویر یا سکرین شاٹ بھیجنے کی اجازت ہے۔");
    }
    e.target.value = "";
}

// وائس ٹرانسکریپشن (Speech to Text)
function initSpeechRecognition() {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
        const Speech = window.SpeechRecognition || window.webkitSpeechRecognition;
        recognition = new Speech();
        recognition.continuous = false;
        recognition.lang = 'ur-PK';

        recognition.onresult = (e) => {
            const transcript = e.results[0][0].transcript;
            document.getElementById("user-input").value = transcript;
            stopSpeech();
        };

        recognition.onerror = () => stopSpeech();
        recognition.onend = () => stopSpeech();
    }
}

function toggleSpeech() {
    if (!recognition) {
        alert("آپ کا براؤزر وائس ان پٹ کو سپورٹ نہیں کرتا۔");
        return;
    }

    const plusBtn = document.getElementById("plus-btn");
    if (!isRecording) {
        recognition.start();
        isRecording = true;
        plusBtn.innerText = "×"; // پلس ضرب بن جائے گا
    } else {
        stopSpeech();
    }
}

function stopSpeech() {
    if (recognition && isRecording) {
        recognition.stop();
    }
    isRecording = false;
    document.getElementById("plus-btn").innerText = "+";
}

// آواز میں سننے کا فنکشن (Text to Speech)
function speakText(text) {
    window.speechSynthesis.cancel();
    // کوڈ بلاکس ہٹا کر بولیں
    const cleanText = text.replace(/```[\s\S]*?```/g, 'کوڈ کا حصہ');
    const utterance = new SpeechSynthesisUtterance(cleanText);
    utterance.lang = "ur-PK";
    window.speechSynthesis.speak(utterance);
      }
          
