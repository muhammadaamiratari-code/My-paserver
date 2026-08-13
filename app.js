/* =========================================================
   MyPA AI Hub
   app.js
   Core application controls
   ========================================================= */

"use strict";


/* =========================================================
   DOM REFERENCES
   ========================================================= */

const chat = document.getElementById("chat");
const input = document.getElementById("messageInput");
const composer = document.getElementById("composer");
const wave = document.getElementById("wave");
const statusEl = document.getElementById("status");
const sendBtn = document.getElementById("sendButton");
const attachEl = document.getElementById("attach");
const bottomBar = document.getElementById("bottomBar");
const toastEl = document.getElementById("toast");


/* =========================================================
   APP STATE
   ========================================================= */

let mediaRecorder = null;
let mediaStream = null;
let chunks = [];

let recognition = null;

let listening = false;
let live = false;

let animFrame = null;
let audioContext = null;
let analyser = null;

let toastTimer = null;

let recognitionRestartTimer = null;


/* =========================================================
   STATUS
   ========================================================= */

function status(text) {

    if (!statusEl) return;

    statusEl.textContent = text;
}


/* =========================================================
   TOAST
   ========================================================= */

function showToast(text, type = "error") {

    if (!toastEl) return;

    toastEl.textContent = text;

    toastEl.className = "toast show " + type;

    clearTimeout(toastTimer);

    toastTimer = setTimeout(() => {

        toastEl.className = "toast";

    }, 4500);
}


/* =========================================================
   CHAT MESSAGE
   ========================================================= */

function addMessage(text, sender = "assistant") {

    if (!chat) return;

    document.getElementById("empty")?.remove();


    if (sender === "assistant") {

        const row = document.createElement("div");

        row.className = "ai-wrap";


        const message = document.createElement("div");

        message.className = "msg ai";

        message.textContent = text;


        const speakButton =
            document.createElement("button");

        speakButton.className = "speak-btn";

        speakButton.textContent = "🔊";

        speakButton.title = "AI جواب سنیں";

        speakButton.setAttribute(
            "aria-label",
            "AI جواب سنیں"
        );


        speakButton.addEventListener(
            "click",
            () => speakText(text)
        );


        row.appendChild(message);

        row.appendChild(speakButton);

        chat.appendChild(row);


    } else {

        const message =
            document.createElement("div");

        message.className = "msg user";

        message.textContent = text;

        chat.appendChild(message);
    }


    chat.scrollTop = chat.scrollHeight;
}


/* =========================================================
   TEXT TO SPEECH
   ========================================================= */

function speakText(text) {

    if (!("speechSynthesis" in window)) {

        status(
            "اس browser میں Text-to-Speech دستیاب نہیں۔"
        );

        return;
    }


    try {

        window.speechSynthesis.cancel();


        const utterance =
            new SpeechSynthesisUtterance(text);


        utterance.lang = "ur-PK";

        utterance.rate = 0.95;

        utterance.pitch = 1;


        window.speechSynthesis.speak(
            utterance
        );


    } catch (error) {

        status(
            "آواز چلانے میں مسئلہ آیا۔"
        );
    }
}


/* =========================================================
   SEND BUTTON
   ========================================================= */

function updateSend() {

    if (!sendBtn || !input) return;


    const hasText =
        input.value.trim().length > 0;


    sendBtn.classList.toggle(
        "active",
        hasText || listening
    );
}


if (input) {

    input.addEventListener(
        "input",
        updateSend
    );


    input.addEventListener(
        "keydown",
        event => {

            if (
                event.key === "Enter" &&
                !event.shiftKey
            ) {

                event.preventDefault();

                sendCurrent();
            }
        }
    );
}


/* =========================================================
   SEND CURRENT MESSAGE
   ========================================================= */

function sendCurrent() {

    if (listening) {

        sendVoice();

        return;
    }


    if (live) {

        status(
            "Live Call جاری ہے — پہلے × دبائیں۔"
        );

        return;
    }


    if (!input) return;


    const text =
        input.value.trim();


    if (!text) return;


    input.value = "";

    updateSend();


    addMessage(
        text,
        "user"
    );


    processMessage(text);
}


/* =========================================================
   CURRENT TEST PROCESSOR
   =========================================================
   Future:
   یہاں server.py / AI API connection آئے گا۔
   ========================================================= */

function processMessage(text) {

    if (!text) return;


    setTimeout(() => {

        addMessage(
            "آپ کا پیغام وصول ہو گیا۔",
            "assistant"
        );

    }, 350);
}


/* =========================================================
   ATTACHMENT MENU
   ========================================================= */

function toggleAttach() {

    if (!attachEl) return;


    if (
        attachEl.classList.contains("open")
    ) {

        closeAttachMenu();


        if (
            history.state &&
            history.state.mypaAttachMenu
        ) {

            try {

                history.back();

            } catch (error) {}
        }


    } else {

        openAttachMenu();
    }
}


function openAttachMenu() {

    if (!attachEl) return;


    attachEl.classList.add("open");


    try {

        history.pushState(
            { mypaAttachMenu: true },
            "",
            window.location.href
        );

    } catch (error) {}
}


function closeAttachMenu() {

    if (!attachEl) return;

    attachEl.classList.remove("open");
}


/* =========================================================
   BROWSER BACK BUTTON
   ========================================================= */

window.addEventListener(
    "popstate",
    () => {

        if (
            attachEl &&
            attachEl.classList.contains("open")
        ) {

            closeAttachMenu();
        }
    }
);


/* =========================================================
   THREE-LINE MAIN MENU
   ========================================================= */

function toggleMenu() {

    const menu =
        document.getElementById("menu");

    const backdrop =
        document.getElementById("backdrop");


    if (!menu || !backdrop) return;


    menu.classList.toggle("open");

    backdrop.classList.toggle("show");
}


/* =========================================================
   NEW CHAT
   ========================================================= */

function newChat() {

    location.reload();
}


/* =========================================================
   VOICE SUBMENU
   ========================================================= */

function toggleVoiceSubmenu() {

    const submenu =
        document.getElementById(
            "voiceSubmenu"
        );

    const arrow =
        document.getElementById(
            "voiceArrow"
        );


    if (!submenu) return;


    submenu.classList.toggle("open");


    if (arrow) {

        arrow.style.transform =
            submenu.classList.contains("open")
                ? "rotate(180deg)"
                : "rotate(0deg)";
    }
}


/* =========================================================
   AUDIO WAVE
   ========================================================= */

function makeWave() {

    if (!wave) return;


    wave.innerHTML = "";


    for (let i = 0; i < 38; i++) {

        const bar =
            document.createElement("i");

        wave.appendChild(bar);
    }
}


function startWave() {

    if (!wave || !mediaStream) return;


    stopWave();

    makeWave();


    const bars =
        [...wave.children];


    try {

        const AudioContext =
            window.AudioContext ||
            window.webkitAudioContext;


        if (!AudioContext) {

            throw new Error(
                "AudioContext unavailable"
            );
        }


        audioContext =
            new AudioContext();


        analyser =
            audioContext.createAnalyser();


        analyser.fftSize = 64;


        const source =
            audioContext.createMediaStreamSource(
                mediaStream
            );


        source.connect(analyser);


        const data =
            new Uint8Array(
                analyser.frequencyBinCount
            );


        function draw() {

            if (!analyser) return;


            analyser.getByteFrequencyData(
                data
            );


            bars.forEach(
                (bar, index) => {

                    const value =
                        4 +
                        (
                            (
                                data[
                                    index %
                                    data.length
                                ] || 0
                            ) / 255
                        ) * 34;


                    bar.style.height =
                        value + "px";
                }
            );


            animFrame =
                requestAnimationFrame(draw);
        }


        draw();


    } catch (error) {

        bars.forEach(
            (bar, index) => {

                bar.style.height =
                    (
                        5 +
                        (index % 5) * 3
                    ) + "px";
            }
        );
    }
}


function stopWave() {

    if (animFrame) {

        cancelAnimationFrame(
            animFrame
        );

        animFrame = null;
    }


    if (audioContext) {

        try {

            audioContext.close();

        } catch (error) {}


        audioContext = null;
    }


    analyser = null;


    if (wave) {

        wave.innerHTML = "";
    }
}


/* =========================================================
   MICROPHONE PERMISSION CHECK
   ========================================================= */

async function checkMicrophonePermission() {

    try {

        if (
            navigator.permissions &&
            navigator.permissions.query
        ) {

            const permission =
                await navigator.permissions.query({
                    name: "microphone"
                });


            if (
                permission.state ===
                "denied"
            ) {

                return false;
            }
        }

    } catch (error) {

        /*
         * کچھ browsers permissions.query
         * support نہیں کرتے۔
         * ایسی صورت میں getUserMedia
         * اصل permission check کرے گا۔
         */
    }


    return true;
}


/* =========================================================
   VOICE RECORDING START
   ========================================================= */

async function startVoice() {

    if (listening || live) return;


    const permissionOK =
        await checkMicrophonePermission();


    if (!permissionOK) {

        showToast(
            "Microphone بند ہے۔ Chrome Site Settings → Permissions → Microphone → Allow کریں، پھر Reload کریں۔",
            "error"
        );


        status(
            "Microphone blocked"
        );


        return;
    }


    try {

        if (
            !navigator.mediaDevices ||
            !navigator.mediaDevices.getUserMedia
        ) {

            throw new Error(
                "Microphone API unavailable"
            );
        }


        mediaStream =
            await navigator.mediaDevices.getUserMedia({
                audio: true
            });


        chunks = [];


        const Recorder =
            window.MediaRecorder;


        if (!Recorder) {

            throw new Error(
                "MediaRecorder unavailable"
            );
        }


        mediaRecorder =
            new Recorder(
                mediaStream
            );


        mediaRecorder.ondataavailable =
            event => {

                if (
                    event.data &&
                    event.data.size > 0
                ) {

                    chunks.push(
                        event.data
                    );
                }
            };


        mediaRecorder.onerror =
            () => {

                showToast(
                    "Voice recording میں مسئلہ آیا۔",
                    "error"
                );
            };


        mediaRecorder.start();


        listening = true;


        if (composer) {

            composer.classList.add(
                "recording"
            );
        }


        updateSend();


        status(
            "🎙️ Voice recording — بولیں، پھر ➤ دبائیں"
        );


        showToast(
            "Microphone connected ✓ اب بولیں۔",
            "ok"
        );


        startWave();


        startRecognition(false);


    } catch (error) {

        cleanupMedia();


        listening = false;


        if (composer) {

            composer.classList.remove(
                "recording"
            );
        }


        updateSend();


        status(
            "Microphone permission درکار ہے۔"
        );


        showToast(
            "Microphone Permission Required — Chrome میں اس site کے لیے Microphone کو Allow کریں، پھر Reload کریں۔",
            "error"
        );
    }
}


/* =========================================================
   SPEECH RECOGNITION
   ========================================================= */

function getSpeechRecognitionClass() {

    return (
        window.SpeechRecognition ||
        window.webkitSpeechRecognition ||
        null
    );
}


function startRecognition(isLive) {

    const SpeechRecognition =
        getSpeechRecognitionClass();


    if (!SpeechRecognition) {

        status(
            "Microphone record ہو رہا ہے، مگر speech-to-text اس browser میں دستیاب نہیں۔"
        );

        return;
    }


    stopRecognition();


    try {

        recognition =
            new SpeechRecognition();


        recognition.lang =
            "ur-PK";


        recognition.continuous =
            Boolean(isLive);


        recognition.interimResults =
            true;


        recognition.onresult =
            event => {

                let transcript = "";


                for (
                    let i = event.resultIndex;
                    i < event.results.length;
                    i++
                ) {

                    transcript +=
                        event.results[i][0]
                            .transcript;
                }


                if (
                    transcript &&
                    input
                ) {

                    input.value =
                        transcript;
                }


                updateSend();
            };


        recognition.onerror =
            event => {

                if (
                    event.error ===
                    "not-allowed"
                ) {

                    status(
                        "Speech recognition permission denied."
                    );

                    return;
                }


                if (
                    event.error ===
                    "aborted"
                ) {

                    return;
                }


                status(
                    "Speech recognition: " +
                    event.error
                );
            };


        recognition.onend =
            () => {

                if (
                    live &&
                    isLive
                ) {

                    scheduleRecognitionRestart();
                }
            };


        recognition.start();


    } catch (error) {

        recognition = null;

        status(
            "Speech recognition شروع نہیں ہو سکی۔"
        );
    }
}


/* =========================================================
   SAFE SPEECH RECOGNITION RESTART
   ========================================================= */

function scheduleRecognitionRestart() {

    clearTimeout(
        recognitionRestartTimer
    );


    recognitionRestartTimer =
        setTimeout(() => {

            if (
                live &&
                !recognition
            ) {

                startRecognition(true);

            } else if (live) {

                try {

                    recognition.start();

                } catch (error) {

                    recognition = null;

                    startRecognition(true);
                }
            }

        }, 250);
}


/* =========================================================
   STOP SPEECH RECOGNITION
   ========================================================= */

function stopRecognition() {

    clearTimeout(
        recognitionRestartTimer
    );


    if (recognition) {

        try {

            recognition.onend = null;

            recognition.stop();

        } catch (error) {}


        recognition = null;
    }
}


/* =========================================================
   SEND VOICE
   ========================================================= */

function sendVoice() {

    if (!mediaRecorder) return;


    if (
        mediaRecorder.state !==
        "recording"
    ) {

        return;
    }


    const recorder =
        mediaRecorder;


    recorder.onstop =
        async () => {

            const mimeType =
                recorder.mimeType ||
                "audio/webm";


            const blob =
                new Blob(
                    chunks,
                    {
                        type: mimeType
                    }
                );


            /*
             * مستقبل میں:
             * یہی blob server.py/API کو
             * بھیجا جائے گا۔
             */


            if (blob.size > 0) {

                addMessage(
                    "Voice recording تیار ہے۔",
                    "user"
                );

            }


            status(
                "Voice capture مکمل ✓"
            );


            const transcript =
                input
                    ? input.value.trim()
                    : "";


            if (input) {

                input.value = "";
            }


            chunks = [];


            updateSend();


            if (transcript) {

                processMessage(
                    transcript
                );
            }
        };


    stopRecording(false);
}


/* =========================================================
   MEDIA CLEANUP
   ========================================================= */

function cleanupMedia() {

    if (mediaStream) {

        try {

            mediaStream
                .getTracks()
                .forEach(track => {

                    try {

                        track.stop();

                    } catch (error) {}
                });

        } catch (error) {}
    }


    mediaStream = null;


    mediaRecorder = null;


    stopWave();
}


/* =========================================================
   STOP VOICE RECORDING
   ========================================================= */

function stopRecording(cancel = false) {

    stopRecognition();


    const recorder =
        mediaRecorder;


    if (
        recorder &&
        recorder.state !== "inactive"
    ) {

        try {

            recorder.stop();

        } catch (error) {}
    }


    cleanupMedia();


    listening = false;


    if (composer) {

        composer.classList.remove(
            "recording"
        );
    }


    updateSend();


    if (cancel) {

        chunks = [];


        if (input) {

            input.value = "";
        }


        status(
            "Voice cancelled"
        );
    }
}


/* =========================================================
   CANCEL CURRENT ACTION
   ========================================================= */

function cancelCurrent() {

    if (live) {

        stopLive();

        return;
    }


    if (listening) {

        stopRecording(true);
    }
}


/* =========================================================
   LIVE CALL START / STOP
   ========================================================= */

async function toggleLive() {

    if (live) {

        stopLive();

        return;
    }


    if (listening) {

        status(
            "پہلے موجودہ Voice recording ختم کریں۔"
        );

        return;
    }


    const permissionOK =
        await checkMicrophonePermission();


    if (!permissionOK) {

        showToast(
            "Live Call کے لیے Microphone blocked ہے۔ Chrome Site Settings میں Microphone کو Allow کریں۔",
            "error"
        );


        status(
            "Microphone blocked"
        );


        return;
    }


    try {

        if (
            !navigator.mediaDevices ||
            !navigator.mediaDevices.getUserMedia
        ) {

            throw new Error(
                "Microphone API unavailable"
            );
        }


        mediaStream =
            await navigator.mediaDevices.getUserMedia({
                audio: true
            });


        live = true;


        if (composer) {

            composer.classList.add(
                "recording"
            );
        }


        status(
            "🔴 Live Call — microphone connected"
        );


        showToast(
            "Live Call microphone connected ✓",
            "ok"
        );


        startWave();


        startRecognition(true);


    } catch (error) {

        cleanupMedia();


        live = false;


        if (composer) {

            composer.classList.remove(
                "recording"
            );
        }


        updateSend();


        status(
            "Live Call کے لیے Microphone permission درکار ہے۔"
        );


        showToast(
            "Live Call کے لیے Microphone Permission Allow کریں، پھر صفحہ Reload کریں۔",
            "error"
        );
    }
}


/* =========================================================
   STOP LIVE CALL
   ========================================================= */

function stopLive() {

    live = false;


    stopRecognition();


    cleanupMedia();


    if (composer) {

        composer.classList.remove(
            "recording"
        );
    }


    status(
        "Live Call ختم ہو گئی"
    );


    updateSend();
}


/* =========================================================
   FILE / IMAGE SELECTION
   ========================================================= */

async function uploadFile(file) {

    if (!file) return;


    closeAttachMenu();


    const maxSize =
        25 * 1024 * 1024;


    if (file.size > maxSize) {

        showToast(
            "File بہت بڑی ہے۔ فی الحال زیادہ سے زیادہ 25MB رکھیں۔",
            "error"
        );

        return;
    }


    addMessage(
        "File/Picture منتخب ہوئی: " +
        file.name,
        "user"
    );


    status(
        "File selected ✓"
    );


    /*
     * Future:
     *
     * یہاں FormData کے ذریعے file
     * server.py کو بھیجی جائے گی۔
     *
     * ابھی اصل upload intentionally
     * نہیں کیا گیا۔
     */
}


/* =========================================================
   MOBILE KEYBOARD
   ========================================================= */

function syncKeyboard() {

    if (
        !window.visualViewport ||
        !bottomBar
    ) {

        return;
    }


    const overlap =
        Math.max(
            0,
            window.innerHeight -
            window.visualViewport.height -
            window.visualViewport.offsetTop
        );


    bottomBar.style.bottom =
        overlap + "px";
}


if (window.visualViewport) {

    window.visualViewport.addEventListener(
        "resize",
        syncKeyboard
    );


    window.visualViewport.addEventListener(
        "scroll",
        syncKeyboard
    );


    syncKeyboard();
}


/* =========================================================
   PAGE VISIBILITY
   =========================================================
   اگر user app/browser سے باہر جائے تو
   microphone غیر ضروری طور پر چلتا نہ رہے۔
   ========================================================= */

document.addEventListener(
    "visibilitychange",
    () => {

        if (
            document.hidden &&
            listening
        ) {

            stopRecording(true);
        }
    }
);


/* =========================================================
   SAFE PAGE UNLOAD CLEANUP
   ========================================================= */

window.addEventListener(
    "beforeunload",
    () => {

        stopRecognition();

        cleanupMedia();

        try {

            window.speechSynthesis?.cancel();

        } catch (error) {}
    }
);


/* =========================================================
   INITIAL UI STATE
========================================================= */

updateSend();

syncKeyboard();


/*
 * جان بوجھ کر یہاں کوئی automatic greeting
 * یا testing message نہیں رکھا گیا۔
 *
 * App کھلنے پر chat screen صاف رہے گی۔
 */
