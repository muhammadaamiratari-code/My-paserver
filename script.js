let history = JSON.parse(localStorage.getItem("mypa_chat_history")) || [];

let isRecording = false;
let recognition = null;
let currentBase64Image = null;
let currentSpeakingBtn = null;

let finalTranscript = "";
let interimTranscript = "";

const SETTINGS_KEY = "mypa_ui_settings";


/* ============================================================
   PAGE START
   ============================================================ */

window.onload = () => {
    loadSettings();
    cleanOldHistory();
    renderHistory();
    initSpeechRecognition();
    setupInputEvents();
    setupSettingsEvents();
};


/* ============================================================
   SETTINGS
   ============================================================ */

function getSettings() {
    const defaults = {
        theme: "light",
        fontSize: "normal",
        voiceTone: "male",
        speechRate: 1,
        responseStyle: "direct"
    };

    try {
        const saved = JSON.parse(
            localStorage.getItem(SETTINGS_KEY)
        );

        if (!saved || typeof saved !== "object") {
            return defaults;
        }

        return {
            ...defaults,
            ...saved
        };

    } catch (err) {
        return defaults;
    }
}


function saveSettings(settings) {
    localStorage.setItem(
        SETTINGS_KEY,
        JSON.stringify(settings)
    );
}


function loadSettings() {
    const settings = getSettings();

    const themeSelect = document.getElementById("theme-select");
    const fontSizeSelect = document.getElementById("font-size-select");
    const voiceTone = document.getElementById("voice-tone");
    const speechRate = document.getElementById("speech-rate");
    const responseStyle = document.getElementById("response-style");

    if (themeSelect) {
        themeSelect.value = settings.theme;
        applyTheme(settings.theme);
    }

    if (fontSizeSelect) {
        fontSizeSelect.value = settings.fontSize;
        applyFontSize(settings.fontSize);
    }

    if (voiceTone) {
        voiceTone.value = settings.voiceTone;
    }

    if (speechRate) {
        speechRate.value = settings.speechRate;
        updateSpeechRateDisplay();
    }

    if (responseStyle) {
        responseStyle.value = settings.responseStyle;
    }
}


function setupSettingsEvents() {
    const themeSelect = document.getElementById("theme-select");
    const fontSizeSelect = document.getElementById("font-size-select");
    const voiceTone = document.getElementById("voice-tone");
    const speechRate = document.getElementById("speech-rate");
    const responseStyle = document.getElementById("response-style");
    const clearHistoryBtn = document.getElementById("clear-history-btn");

    if (themeSelect) {
        themeSelect.addEventListener("change", () => {
            const settings = getSettings();

            settings.theme = themeSelect.value;

            saveSettings(settings);
            applyTheme(settings.theme);
        });
    }

    if (fontSizeSelect) {
        fontSizeSelect.addEventListener("change", () => {
            const settings = getSettings();

            settings.fontSize = fontSizeSelect.value;

            saveSettings(settings);
            applyFontSize(settings.fontSize);
        });
    }

    if (voiceTone) {
        voiceTone.addEventListener("change", () => {
            const settings = getSettings();

            settings.voiceTone = voiceTone.value;

            saveSettings(settings);
        });
    }

    if (speechRate) {
        speechRate.addEventListener("input", () => {
            const settings = getSettings();

            settings.speechRate = Number(
                speechRate.value
            );

            saveSettings(settings);
            updateSpeechRateDisplay();
        });
    }

    if (responseStyle) {
        responseStyle.addEventListener("change", () => {
            const settings = getSettings();

            settings.responseStyle = responseStyle.value;

            saveSettings(settings);
        });
    }

    if (clearHistoryBtn) {
        clearHistoryBtn.addEventListener(
            "click",
            clearChatHistory
        );
    }
}


function applyTheme(theme) {
    document.body.classList.toggle(
        "dark-mode",
        theme === "dark"
    );
}


function applyFontSize(fontSize) {
    document.body.classList.toggle(
        "large-font",
        fontSize === "large"
    );
}


function updateSpeechRateDisplay() {
    const rateInput = document.getElementById(
        "speech-rate"
    );

    const rateDisplay = document.getElementById(
        "speech-rate-value"
    );

    if (!rateInput || !rateDisplay) {
        return;
    }

    const rate = Number(rateInput.value);

    rateDisplay.textContent =
        `${rate.toFixed(1)}x`;
}


/* ============================================================
   CHAT HISTORY
   ============================================================ */

function cleanOldHistory() {
    const oneWeekInMs =
        7 * 24 * 60 * 60 * 1000;

    const now = Date.now();

    history = history.filter(item => {
        return (
            item &&
            typeof item.timestamp === "number" &&
            (now - item.timestamp) < oneWeekInMs
        );
    });

    localStorage.setItem(
        "mypa_chat_history",
        JSON.stringify(history)
    );
}


function renderHistory() {
    const chatContainer =
        document.getElementById("chat-container");

    if (!chatContainer) {
        return;
    }

    chatContainer.innerHTML = "";

    history.forEach(item => {
        appendMessageUI(
            item.sender,
            item.text,
            item.image,
            false
        );
    });
}


/* ============================================================
   MESSAGE UI
   ============================================================ */

function appendMessageUI(
    sender,
    text,
    imageSrc = null,
    save = true
) {
    const chatContainer =
        document.getElementById("chat-container");

    if (!chatContainer) {
        return;
    }

    const msgDiv =
        document.createElement("div");

    msgDiv.classList.add(
        "message",
        sender === "user"
            ? "user-message"
            : "bot-message"
    );

    if (imageSrc) {
        const img =
            document.createElement("img");

        img.src = imageSrc;
        img.classList.add("chat-image");
        img.alt = "Uploaded image";

        msgDiv.appendChild(img);
    }

    if (text) {
        const textDiv =
            document.createElement("div");

        textDiv.innerHTML =
            formatCodeText(text);

        msgDiv.appendChild(textDiv);
    }

    if (sender === "bot" && text) {
        const actionContainer =
            document.createElement("div");

        actionContainer.classList.add(
            "message-actions"
        );

        const copyBtn =
            document.createElement("button");

        copyBtn.type = "button";
        copyBtn.classList.add(
            "action-btn",
            "copy-btn"
        );

        copyBtn.innerText = "📋";
        copyBtn.title = "کاپی کریں";

        copyBtn.onclick = () => {
            copyToClipboard(
                text,
                copyBtn
            );
        };

        actionContainer.appendChild(
            copyBtn
        );


        const speakBtn =
            document.createElement("button");

        speakBtn.type = "button";
        speakBtn.classList.add(
            "action-btn",
            "speaker-btn"
        );

        speakBtn.innerText = "🔊";
        speakBtn.title = "سُنیں";

        speakBtn.onclick = () => {
            toggleTextSpeech(
                text,
                speakBtn
            );
        };

        actionContainer.appendChild(
            speakBtn
        );

        msgDiv.appendChild(
            actionContainer
        );
    }

    chatContainer.appendChild(msgDiv);

    chatContainer.scrollTop =
        chatContainer.scrollHeight;

    if (save) {
        history.push({
            sender: sender,
            text: text,
            image: imageSrc,
            timestamp: Date.now()
        });

        localStorage.setItem(
            "mypa_chat_history",
            JSON.stringify(history)
        );
    }
}


/* ============================================================
   COPY
   ============================================================ */

function copyToClipboard(
    text,
    btnElement
) {
    if (
        navigator.clipboard &&
        navigator.clipboard.writeText
    ) {
        navigator.clipboard.writeText(text)
            .then(() => {
                btnElement.innerText = "✔️";

                setTimeout(() => {
                    btnElement.innerText = "📋";
                }, 2000);
            })
            .catch(() => {
                fallbackCopyTextToClipboard(
                    text,
                    btnElement
                );
            });

        return;
    }

    fallbackCopyTextToClipboard(
        text,
        btnElement
    );
}


function fallbackCopyTextToClipboard(
    text,
    btnElement
) {
    const textArea =
        document.createElement("textarea");

    textArea.value = text;
    textArea.style.position = "fixed";
    textArea.style.left = "-9999px";

    document.body.appendChild(
        textArea
    );

    textArea.focus();
    textArea.select();

    try {
        document.execCommand("copy");

        btnElement.innerText = "✔️";

        setTimeout(() => {
            btnElement.innerText = "📋";
        }, 2000);

    } catch (err) {
        alert("کاپی نہیں ہو سکا۔");
    }

    document.body.removeChild(
        textArea
    );
}


/* ============================================================
   TEXT SPEECH
   ============================================================ */

function toggleTextSpeech(
    text,
    btnElement
) {
    if (
        !window.speechSynthesis ||
        !("SpeechSynthesisUtterance" in window)
    ) {
        alert(
            "آپ کا براؤزر Text-to-Speech کو سپورٹ نہیں کرتا۔"
        );
        return;
    }

    if (
        window.speechSynthesis.speaking &&
        currentSpeakingBtn === btnElement
    ) {
        window.speechSynthesis.cancel();

        btnElement.innerText = "🔊";
        currentSpeakingBtn = null;

        return;
    }

    window.speechSynthesis.cancel();

    if (currentSpeakingBtn) {
        currentSpeakingBtn.innerText = "🔊";
    }

    const cleanText =
        text.replace(
            /```[\s\S]*?```/g,
            "کوڈ کا حصہ"
        );

    const utterance =
        new SpeechSynthesisUtterance(
            cleanText
        );

    const settings = getSettings();

    utterance.lang = "ur-PK";
    utterance.rate = Number(
        settings.speechRate
    );

    const selectedVoice =
        findPreferredVoice(
            settings.voiceTone
        );

    if (selectedVoice) {
        utterance.voice =
            selectedVoice;
    }

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

    window.speechSynthesis.speak(
        utterance
    );
}


function findPreferredVoice(
    tone
) {
    if (
        !window.speechSynthesis ||
        !window.speechSynthesis.getVoices
    ) {
        return null;
    }

    const voices =
        window.speechSynthesis.getVoices();

    if (!voices.length) {
        return null;
    }

    const urduVoices =
        voices.filter(voice => {
            return (
                voice.lang &&
                voice.lang
                    .toLowerCase()
                    .startsWith("ur")
            );
        });

    const candidates =
        urduVoices.length
            ? urduVoices
            : voices;

    const maleWords = [
        "male",
        "man",
        "ahmed",
        "hamza",
        "hassan"
    ];

    const femaleWords = [
        "female",
        "woman",
        "sara",
        "aisha",
        "ayesha"
    ];

    const keywords =
        tone === "female"
            ? femaleWords
            : maleWords;

    const preferred =
        candidates.find(voice => {
            const name =
                voice.name
                    .toLowerCase();

            return keywords.some(
                keyword =>
                    name.includes(keyword)
            );
        });

    return preferred || candidates[0];
}


/* ============================================================
   CODE FORMATTING
   ============================================================ */

function escapeHtml(text) {
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


function formatCodeText(text) {
    const codeBlocks = [];

    let safeText = String(text).replace(
        /```([\s\S]*?)```/g,
        function(match, code) {
            const index =
                codeBlocks.length;

            codeBlocks.push(
                escapeHtml(code)
            );

            return `___CODE_BLOCK_${index}___`;
        }
    );

    safeText = escapeHtml(
        safeText
    );

    codeBlocks.forEach(
        (code, index) => {
            safeText = safeText.replace(
                `___CODE_BLOCK_${index}___`,
                `<pre><code>${code}</code></pre>`
            );
        }
    );

    return safeText.replace(
        /\n/g,
        "<br>"
    );
}


/* ============================================================
   INPUT EVENTS
   ============================================================ */

function setupInputEvents() {
    const inputEl =
        document.getElementById(
            "user-input"
        );

    if (!inputEl) {
        return;
    }

    inputEl.addEventListener(
        "keydown",
        function(e) {
            if (
                e.key === "Enter" &&
                !e.shiftKey
            ) {
                e.preventDefault();

                sendPayload();
            }
        }
    );
}


/* ============================================================
   SEND MESSAGE
   ============================================================ */

async function sendPayload() {
    const inputEl =
        document.getElementById(
            "user-input"
        );

    if (!inputEl) {
        return;
    }

    const text =
        inputEl.value.trim();

    const imageToSend =
        currentBase64Image;

    if (!text && !imageToSend) {
        return;
    }

    if (isRecording) {
        stopSpeech();
    }

    inputEl.value = "";

    const tempImage =
        currentBase64Image;

    currentBase64Image = null;

    appendMessageUI(
        "user",
        text,
        tempImage,
        true
    );

    try {
        const response =
            await fetch(
                "/api/chat",
                {
                    method: "POST",
                    headers: {
                        "Content-Type":
                            "application/json"
                    },
                    body: JSON.stringify({
                        message: text,
                        image: tempImage,
                        history: history
                    })
                }
            );

        if (!response.ok) {
            throw new Error(
                `HTTP ${response.status}`
            );
        }

        const data =
            await response.json();

        if (data.success) {
            appendMessageUI(
                "bot",
                data.reply,
                null,
                true
            );
        } else {
            appendMessageUI(
                "bot",
                "خطا: " +
                (data.reply ||
                    "نامعلوم خرابی"),
                null,
                false
            );
        }

    } catch (err) {
        appendMessageUI(
            "bot",
            "سرور سے رابطہ قائم نہیں ہو سکا۔",
            null,
            false
        );

        console.error(
            "Chat request error:",
            err
        );
    }
}


/* ============================================================
   IMAGE
   ============================================================ */

function handlePlusClick() {
    if (isRecording) {
        stopSpeech();
        return;
    }

    const imageInput =
        document.getElementById(
            "image-input"
        );

    if (imageInput) {
        imageInput.click();
    }
}


function handleImageSelected(e) {
    const file =
        e.target.files &&
        e.target.files[0];

    if (
        file &&
        file.type.startsWith("image/")
    ) {
        const reader =
            new FileReader();

        reader.onload =
            function(event) {
                currentBase64Image =
                    event.target.result;

                sendPayload();
            };

        reader.onerror = () => {
            alert(
                "تصویر پڑھنے میں مسئلہ آیا۔"
            );
        };

        reader.readAsDataURL(file);

    } else if (file) {
        alert(
            "صرف تصویر یا سکرین شاٹ بھیجنے کی اجازت ہے۔"
        );
    }

    e.target.value = "";
}


/* ============================================================
   VOICE RECOGNITION
   ============================================================ */

function initSpeechRecognition() {
    if (
        !(
            "webkitSpeechRecognition" in
            window
        ) &&
        !(
            "SpeechRecognition" in
            window
        )
    ) {
        recognition = null;
        return;
    }

    const Speech =
        window.SpeechRecognition ||
        window.webkitSpeechRecognition;

    recognition = new Speech();

    /*
     * continuous = true رکھا گیا ہے تاکہ ایک ہی
     * recording میں لمبا جملہ بول سکیں۔
     *
     * لیکن اب final اور interim transcript الگ
     * رکھے جاتے ہیں، اس لیے پچھلا transcript
     * دوبارہ append نہیں ہوگا۔
     */
    recognition.continuous = true;
    recognition.interimResults = true;
    recognition.lang = "ur-PK";


    recognition.onstart = () => {
        isRecording = true;
        updateRecordingUI(true);
    };


    recognition.onresult = (event) => {
        let newFinalText = "";
        let newInterimText = "";

        for (
            let i = event.resultIndex;
            i < event.results.length;
            i++
        ) {
            const result =
                event.results[i];

            const transcript =
                result[0].transcript;

            if (result.isFinal) {
                newFinalText +=
                    transcript + " ";
            } else {
                newInterimText +=
                    transcript;
            }
        }

        /*
         * صرف نئے final results شامل کریں۔
         * پرانے results دوبارہ شامل نہیں ہوں گے۔
         */
        if (newFinalText) {
            finalTranscript +=
                newFinalText;
        }

        interimTranscript =
            newInterimText;

        const inputEl =
            document.getElementById(
                "user-input"
            );

        if (inputEl) {
            inputEl.value =
                (
                    finalTranscript +
                    interimTranscript
                ).trim();
        }
    };


    recognition.onerror = (event) => {
        console.warn(
            "Speech recognition error:",
            event.error
        );

        /*
         * no-speech یا aborted کے بعد
         * خود سے دوبارہ start نہیں کریں گے۔
         */
        stopSpeech();
    };


    recognition.onend = () => {
        /*
         * جان بوجھ کر recognition.start()
         * یہاں ن
