let history = JSON.parse(localStorage.getItem("mypa_chat_history")) || [];
let isRecording = false;
let recognition = null;
let currentBase64Image = null;
let currentSpeakingBtn = null; // اسپیکر کا آن/آف ٹریک رکھنے کے لیے

window.onload = () => {
    cleanOldHistory();
    renderHistory();
    initSpeechRecognition(); // آپ کا اصل وائس کوڈ بلاتعطل چلے گا
    setupInputEvents();
};

// سینڈ بٹن اور کی بورڈ ان پٹ کے ایونٹس (100% گارنٹی شدہ)
function setupInputEvents() {
    const inputEl = document.getElementById("user-input");

    if (inputEl) {
        // keydown کی بورڈ ٹائپنگ اور کاپی پیسٹ دونوں کے Enter کو فوری بھیجے گا
        inputEl.addEventListener("keydown", function(e) {
            if (e.key === "Enter" && !e.shiftKey) {
                e.preventDefault();
                sendPayload();
            }
        });
    }
}

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

    // AI کے ہر میسج کے ساتھ کاپی اور اسپیکر کا آپشن
    if (sender === "bot" && text) {
        const actionContainer = document.createElement("div");
        actionContainer.classList.add("message-actions");
        actionContainer.style.marginTop = "6px";
        actionContainer.style.display = "flex";
        actionContainer.style.gap = "8px";

        // 1. کاپی بٹن
        const copyBtn = document.createElement("button");
        copyBtn.classList.add("action-btn", "copy-btn");
        copyBtn.innerText = "📋";
        copyBtn.title = "کاپی کریں";
        copyBtn.onclick = () => copyToClipboard(text, copyBtn);
        actionContainer.appendChild(copyBtn);

        // 2. اسپیکر آن/آف بٹن
        const speakBtn = document.createElement("button");
        speakBtn.classList.add("action-btn", "speaker-btn");
        speakBtn.innerText = "🔊";
        speakBtn.title = "سُنیں";
        speakBtn.onclick = () => toggleTextSpeech(text, speakBtn);
        actionContainer.appendChild(speakBtn);

        msgDiv.appendChild(actionContainer);
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

// ٹیکسٹ کاپی کرنے کا مکمل فنکشن
function copyToClipboard(text, btnElement) {
    if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(text).then(() => {
            btnElement.innerText = "✔️";
            setTimeout(() => { btnElement.innerText = "📋"; }, 2000);
        }).catch(() => {
            fallbackCopyTextToClipboard(text, btnElement);
        });
    } else {
        fallbackCopyTextToClipboard(text, btnElement);
    }
}

// کاپی کا متبادل طریقہ (پرانے موبائل ویب ویوز کے لیے)
function fallbackCopyTextToClipboard(text, btnElement) {
    const textArea = document.createElement("textarea");
    textArea.value = text;
    textArea.style.position = "fixed";
    textArea.style.left = "-9999px";
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();
    try {
        document.execCommand('copy');
        btnElement.innerText = "✔️";
        setTimeout(() => { btnElement.innerText = "📋"; }, 2000);
    } catch (err) {
        alert("کاپی نہیں ہو سکا۔");
    }
    document.body.removeChild(textArea);
}

// اسپیکر کا آن اور آف (Toggle) کرنا
function toggleTextSpeech(text, btnElement) {
    // اگر وہی اسپیکر چل رہا ہے تو دوبارہ پریس کرنے پر فوراً بند کر دیں
    if (window.speechSynthesis.speaking && currentSpeakingBtn === btnElement) {
        window.speechSynthesis.cancel();
        btnElement.innerText = "🔊";
        currentSpeakingBtn = null;
        return;
    }

    // اگر پہلے سے کوئی اور آواز چل رہی تھی تو اسے روک دیں
    window.speechSynthesis.cancel();
    if (currentSpeakingBtn) {
        currentSpeakingBtn.innerText = "🔊";
    }

    const cleanText = text.replace(/```[\s\S]*?```/g, 'کوڈ کا حصہ');
    const utterance = new SpeechSynthesisUtterance(cleanText);
    utterance.lang = "ur-PK";

    utterance.onend = () => {
        btnElement.innerText = "🔊";
        currentSpeakingBtn = null;
    };

    utterance.onerror = () => {
        btnElement.innerText = "🔊";
        currentSpeakingBtn = null;
    };

    btnElement.innerText = "⏹️";
    currentSpeakingBtn = btnElement;
    window.speechSynthesis.speak(utterance);
}

// کوڈ بلاکس کی فارمیٹنگ
function formatCodeText(text) {
    return text.replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>');
}

// پیغام سرور کو بھیجنا (آپ کا اپنا اصلی فنکشن)
async function sendPayload() {
    const inputEl = document.getElementById("user-input");
    const text = inputEl.value.trim();
    const imageToSend = currentBase64Image;

    if (!text && !imageToSend) return;

    if (isRecording) {
        stopSpeech();
    }

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

// پلس / ضرب بٹن کی لاجک (آپ کا اصلی کوڈ)
function handlePlusClick() {
    if (isRecording) {
        stopSpeech();
    } else {
        document.getElementById("image-input").click();
    }
}

// صرف تصویر اپلوڈ کی ہینڈلنگ (آپ کا اصلی کوڈ)
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

// وائس ٹرانسکریپشن - آپ کا بالکل وہی اصل اور ٹھیک کوڈ
function initSpeechRecognition() {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
        const Speech = window.SpeechRecognition || window.webkitSpeechRecognition;
        recognition = new Speech();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'ur-PK';

        recognition.onresult = (e) => {
            let transcript = '';
            for (let i = e.resultIndex; i < e.results.length; ++i) {
                transcript += e.results[i][0].transcript;
            }
            document.getElementById("user-input").value = transcript;
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

    if (!isRecording) {
        recognition.start();
        isRecording = true;
        document.getElementById("plus-btn").innerText = "×";
        document.getElementById("mic-btn").classList.add("recording");
        document.getElementById("voice-wave").classList.add("active");
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
    document.getElementById("mic-btn").classList.remove("recording");
    document.getElementById("voice-wave").classList.remove("active");
}
