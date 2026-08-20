let history = JSON.parse(localStorage.getItem("mypa_chat_history")) || [];
let isRecording = false;
let recognition = null;
let currentBase64Image = null;
let isVoiceCallActive = false;
let isSpeaking = false;

window.onload = () => {
    cleanOldHistory();
    renderHistory();
    initSpeechRecognition();
    updateSendButton();
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

    if (!text && !imageToSend) {
        return;
    }

    inputEl.value = "";
    const tempImage = currentBase64Image;
    currentBase64Image = null;

    updateSendButton();
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
    if (e.key === "Enter" && !e.shiftKey) {
        e.preventDefault();
        sendPayload();
    }
}

function updateSendButton() {
    const inputEl = document.getElementById("user-input");
    const sendBtn = document.getElementById("send-btn");

    if (inputEl.value.trim() || currentBase64Image) {
        sendBtn.classList.add("active");
    } else {
        sendBtn.classList.remove("active");
    }
}

// پلس / ضرب بٹن کی لاجک
function handlePlusClick() {
    if (isRecording) {
        stopSpeech();
    } else {
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
            updateSendButton();
            sendPayload();
        };

        reader.readAsDataURL(file);
    } else {
        alert("صرف تصویر یا سکرین شاٹ بھیجنے کی اجازت ہے۔");
    }

    e.target.value = "";
}

// وائس ٹرانسکریپشن
function initSpeechRecognition() {
    if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
        const Speech = window.SpeechRecognition || window.webkitSpeechRecognition;

        recognition = new Speech();

        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'ur-PK';

        recognition.onstart = () => {
            isRecording = true;
            updateRecordingUI();
        };

        recognition.onresult = (e) => {
            let finalText = "";

            for (let i = e.resultIndex; i < e.results.length; i++) {
                if (e.results[i].isFinal) {
                    finalText += e.results[i][0].transcript;
                }
            }

            if (!finalText.trim()) {
                return;
            }

            const inputEl = document.getElementById("user-input");

            if (isVoiceCallActive) {
                handleVoiceCallMessage(finalText.trim());
            } else {
                inputEl.value = (inputEl.value + " " + finalText).trim();
                updateSendButton();
            }
        };

        recognition.onerror = (e) => {
            if (e.error === "not-allowed" || e.error === "service-not-allowed") {
                isRecording = false;
                isVoiceCallActive = false;
                updateRecordingUI();
                updateCallButton();
                return;
            }

            if (isRecording) {
                restartRecognition();
            }
        };

        recognition.onend = () => {
            if (isRecording && !isSpeaking) {
                restartRecognition();
            } else {
                updateRecordingUI();
            }
        };
    }
}

function toggleSpeech() {
    if (!recognition) {
        alert("آپ کا براؤزر وائس ان پٹ کو سپورٹ نہیں کرتا۔");
        return;
    }

    if (!isRecording) {
        startSpeech();
    } else {
        stopSpeech();
    }
}

function startSpeech() {
    if (!recognition || isRecording) {
        return;
    }

    try {
        recognition.start();
    } catch (err) {
        restartRecognition();
    }
}

function restartRecognition() {
    if (!recognition || !isRecording || isSpeaking) {
        return;
    }

    try {
        recognition.stop();
    } catch (err) {
    }

    setTimeout(() => {
        if (isRecording && !isSpeaking) {
            try {
                recognition.start();
            } catch (err) {
            }
        }
    }, 250);
}

function stopSpeech() {
    isRecording = false;

    if (recognition) {
        try {
            recognition.stop();
        } catch (err) {
        }
    }

    updateRecordingUI();
}

function updateRecordingUI() {
    const micBtn = document.getElementById("mic-btn");
    const plusBtn = document.getElementById("plus-btn");

    if (isRecording) {
        micBtn.classList.add("recording");
        plusBtn.classList.add("recording");

        if (!document.getElementById("voice-waves")) {
            const waves = document.createElement("span");
            waves.id = "voice-waves";
            waves.innerHTML = "<i></i><i></i><i></i><i></i><i></i>";
            micBtn.appendChild(waves);
        }
    } else {
        micBtn.classList.remove("recording");
        plusBtn.classList.remove("recording");

        const waves = document.getElementById("voice-waves");
        if (waves) {
            waves.remove();
        }
    }
}

// AI وائس کال
function toggleVoiceCall() {
    if (!recognition) {
        alert("آپ کا براؤزر وائس کال کے لیے Speech Recognition کو سپورٹ نہیں کرتا۔");
        return;
    }

    if (isVoiceCallActive) {
        stopVoiceCall();
    } else {
        startVoiceCall();
    }
}

function startVoiceCall() {
    isVoiceCallActive = true;
    updateCallButton();

    if (!isRecording) {
        startSpeech();
    }
}

function stopVoiceCall() {
    isVoiceCallActive = false;
    isSpeaking = false;

    window.speechSynthesis.cancel();

    stopSpeech();
    updateCallButton();
}

function updateCallButton() {
    const callBtn = document.getElementById("call-btn");

    if (isVoiceCallActive) {
        callBtn.classList.add("active");
        callBtn.innerText = "☎";
    } else {
        callBtn.classList.remove("active");
        callBtn.innerText = "☎";
    }
}

async function handleVoiceCallMessage(text) {
    if (!isVoiceCallActive || !text) {
        return;
    }

    if (isSpeaking) {
        return;
    }

    const inputEl = document.getElementById("user-input");
    inputEl.value = "";

    appendMessageUI("user", text, null, true);

    if (isRecording) {
        try {
            recognition.stop();
        } catch (err) {
        }
    }

    isSpeaking = true;
    isRecording = false;
    updateRecordingUI();

    try {
        const response = await fetch("/api/chat", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                message: text,
                image: null,
                history: history
            })
        });

        const data = await response.json();

        if (data.success) {
            appendMessageUI("bot", data.reply, null, true);
            await speakVoiceCallReply(data.reply);
        } else {
            appendMessageUI("bot", "خطا: " + data.reply, null, false);
        }
    } catch (err) {
        appendMessageUI("bot", "سرور سے رابطہ قائم نہیں ہو سکا۔", null, false);
    }

    isSpeaking = false;

    if (isVoiceCallActive) {
        isRecording = true;
        updateRecordingUI();
        startSpeech();
    }
}

function speakVoiceCallReply(text) {
    return new Promise((resolve) => {
        window.speechSynthesis.cancel();

        const cleanText = text.replace(/```[\s\S]*?```/g, 'کوڈ کا حصہ');
        const utterance = new SpeechSynthesisUtterance(cleanText);

        utterance.lang = "ur-PK";

        utterance.onend = () => {
            resolve();
        };

        utterance.onerror = () => {
            resolve();
        };

        window.speechSynthesis.speak(utterance);
    });
}

// آواز میں سننے کا فنکشن
function speakText(text) {
    window.speechSynthesis.cancel();

    const cleanText = text.replace(/```[\s\S]*?```/g, 'کوڈ کا حصہ');
    const utterance = new SpeechSynthesisUtterance(cleanText);

    utterance.lang = "ur-PK";
    window.speechSynthesis.speak(utterance);
}
