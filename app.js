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
const attachEl = document.getElementById("attachmentMenu");
const bottomBar = document.getElementById("bottomBar");
const toastEl = document.getElementById("toast");

const plusButton = document.getElementById("plusButton");
const micButton = document.getElementById("micButton");
const liveButton = document.getElementById("liveButton");
const cancelButton = document.getElementById("cancelButton");
const newChatButton = document.getElementById("newChatButton");

const menuButton = document.getElementById("menuButton");
const mainMenu = document.getElementById("mainMenu");
const menuBackdrop = document.getElementById("menuBackdrop");
const menuSearch = document.getElementById("menuSearch");

const moreButton = document.getElementById("moreButton");
const moreMenu = document.getElementById("moreMenu");
const moreBackdrop = document.getElementById("moreBackdrop");

const voiceMenuItem = document.getElementById("voiceMenuItem");
const voiceSubmenu = document.getElementById("voiceSubmenu");
const voiceArrow = document.getElementById("voiceArrow");


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
let audioSource = null;

let toastTimer = null;
let recognitionRestartTimer = null;


/* =========================================================
   STATUS / TOAST
   ========================================================= */

function status(text) {

    if (!statusEl) return;

    statusEl.textContent = text || "";
}


function showToast(text, type = "error") {

    if (!toastEl) return;

    toastEl.textContent = text || "";

    toastEl.className =
        "toast show " + type;

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

        const row =
            document.createElement("div");

        row.className = "ai-wrap";


        const message =
            document.createElement("div");

        message.className = "msg ai";

        message.textContent = text;


        const speakButton =
            document.createElement("button");

        speakButton.className = "speak-btn";

        speakButton.type = "button";

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


    chat.scrollTop =
        chat.scrollHeight;
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


    if (micButton) {

        micButton.setAttribute(
            "aria-pressed",
            String(listening)
        );
    }


    if (liveButton) {

        liveButton.setAttribute(
            "aria-pressed",
            String(live)
        );
    }
}


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
   CURRENT TEST PROCESSOR
   =========================================================
   IMPORTANT:
   This is still a frontend test processor.
   It is NOT an AI/server connection yet.
   Future server.py / AI API connection belongs here.
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

function setAttachState(open) {

    if (!attachEl || !plusButton) return;


    attachEl.classList.toggle(
        "open",
        open
    );


    attachEl.setAttribute(
        "aria-hidden",
        String(!open)
    );


    plusButton.setAttribute(
        "aria-expanded",
        String(open)
    );
}


function toggleAttach() {

    if (!attachEl) return;


    setAttachState(
        !attachEl.classList.contains("open")
    );
}


function openAttachMenu() {

    setAttachState(true);
}


function closeAttachMenu() {

    setAttachState(false);
}


if (plusButton) {

    plusButton.addEventListener(
        "click",
        event => {

            event.stopPropagation();

            toggleAttach();
        }
    );
}


/* =========================================================
   MAIN 3-LINE MENU
   ========================================================= */

function setMainMenuState(open) {

    if (
        !mainMenu ||
        !menuBackdrop ||
        !menuButton
    ) return;


    mainMenu.classList.toggle(
        "open",
        open
    );


    menuBackdrop.classList.toggle(
        "show",
        open
    );


    mainMenu.setAttribute(
        "aria-hidden",
        String(!open)
    );


    menuBackdrop.setAttribute(
        "aria-hidden",
        String(!open)
    );


    menuButton.setAttribute(
        "aria-expanded",
        String(open)
    );
}


function toggleMenu() {

    if (!mainMenu) return;


    setMainMenuState(
        !mainMenu.classList.contains("open")
    );
}


function closeMenu() {

    setMainMenuState(false);
}


if (menuButton) {

    menuButton.addEventListener(
        "click",
        event => {

            event.stopPropagation();

            toggleMenu();
        }
    );
}


if (menuBackdrop) {

    menuBackdrop.addEventListener(
        "click",
        closeMenu
    );
}


/* =========================================================
   MORE / 3-DOT MENU
   ========================================================= */

function setMoreMenuState(open) {

    if (
        !moreMenu ||
        !moreBackdrop ||
        !moreButton
    ) return;


    moreMenu.classList.toggle(
        "open",
        open
    );


    moreBackdrop.classList.toggle(
        "show",
        open
    );


    moreMenu.setAttribute(
        "aria-hidden",
        String(!open)
    );


    moreBackdrop.setAttribute(
        "aria-hidden",
        String(!open)
    );


    moreButton.setAttribute(
        "aria-expanded",
        String(open)
    );
}


function toggleMoreMenu() {

    if (!moreMenu) return;


    setMoreMenuState(
        !moreMenu.classList.contains("open")
    );
}


function closeMoreMenu() {

    setMoreMenuState(false);
}


if (moreButton) {

    moreButton.addEventListener(
        "click",
        event => {

            event.stopPropagation();

            toggleMoreMenu();
        }
    );
}


if (moreBackdrop) {

    moreBackdrop.addEventListener(
        "click",
        closeMoreMenu
    );
}


/* =========================================================
   NEW CHAT
   ========================================================= */

function newChat() {

    location.reload();
}


if (newChatButton) {

    newChatButton.addEventListener(
        "click",
        newChat
    );
}


/* =========================================================
   VOICE SUBMENU
   ========================================================= */

function toggleVoiceSubmenu() {

    if (!voiceSubmenu) return;


    const open =
        !voiceSubmenu.classList.contains("open");


    voiceSubmenu.classList.toggle(
        "open",
        open
    );


    voiceSubmenu.setAttribute(
        "aria-hidden",
        String(!open)
    );


    if (voiceMenuItem) {

        voiceMenuItem.setAttribute(
            "aria-expanded",
            String(open)
        );
    }


    if (voiceArrow) {

        voiceArrow.style.transform =
            open
                ? "rotate(180deg)"
                : "rotate(0deg)";
    }
}


if (voiceMenuItem) {

    voiceMenuItem.addEventListener(
        "click",
        event => {

            event.stopPropagation();

            toggleVoiceSubmenu();
        }
    );
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

        const AudioContextClass =
            window.AudioContext ||
            window.webkitAudioContext;


        if (!AudioContextClass) {

            throw new Error(
                "AudioContext unavailable"
            );
        }


        audioContext =
            new AudioContextClass();


        analyser =
            audioContext.createAnalyser();


        analyser.fftSize = 64;


        audioSource =
            audioContext.createMediaStreamSource(
                mediaStream
            );


        audioSource.connect(
            analyser
        );


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


    if (audioSource) {

        try {

            audioSource.disconnect();

        } catch (error) {}

        audioSource = null;
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


if (micButton) {

    micButton.addEventListener(
        "click",
        () => {

            if (listening) {

                sendVoice();

            } else {

                startVoice();
            }
        }
    );
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

            if (!live) return;


            if (!recognition) {

                startRecognition(true);

                return;
            }


            try {

                recognition.start();

            } catch (error) {

                recognition = null;

                startRecognition(true);
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
             * Future:
             * یہی blob server.py / AI API کو
             * بھیجا جائے گا۔
             *
             * ابھی اصل audio upload
             * intentionally نہیں کیا گیا۔
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


            cleanupMedia();
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
        recorder.state !==
        "inactive"
    ) {

        try {

            recorder.stop();

        } catch (error) {}
    }


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


        cleanupMedia();


        return;
    }


    if (
        !recorder ||
        recorder.state ===
        "inactive"
    ) {

        cleanupMedia();
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


if (cancelButton) {

    cancelButton.addEventListener(
        "click",
        cancelCurrent
    );
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


        updateSend();


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


if (liveButton) {

    liveButton.addEventListener(
        "click",
        toggleLive
    );
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
     * server.py / AI API کو بھیجی جائے گی۔
     *
     * ابھی اصل upload intentionally
     * نہیں کیا گیا۔
     */
}


/* =========================================================
   FILE INPUTS
   ========================================================= */

const cameraInput =
    document.getElementById("cameraInput");

const photoInput =
    document.getElementById("photoInput");

const fileInput =
    document.getElementById("fileInput");


function bindFileInput(element) {

    if (!element) return;


    element.addEventListener(
        "change",
        event => {

            const file =
                event.target.files?.[0];


            if (file) {

                uploadFile(file);
            }


            event.target.value = "";
        }
    );
}


bindFileInput(cameraInput);

bindFileInput(photoInput);

bindFileInput(fileInput);


/* =========================================================
   ATTACHMENT ACTIONS
   ========================================================= */

document.querySelectorAll(
    "#attachmentMenu [data-action]"
).forEach(button => {

    button.addEventListener(
        "click",
        () => {

            const action =
                button.dataset.action;


            closeAttachMenu();


            if (
                action ===
                "scan-document"
            ) {

                status(
                    "Scan Document ابھی frontend placeholder ہے۔"
                );


            } else if (
                action ===
                "tools"
            ) {

                status(
                    "Tools menu ابھی frontend placeholder ہے۔"
                );


            } else if (
                action ===
                "deep-research"
            ) {

                status(
                    "Deep Research ابھی frontend placeholder ہے۔"
                );
            }
        }
    );
});


/* =========================================================
   MAIN MENU ACTIONS
   ========================================================= */

function handleAction(action) {

    if (!action) return;


    switch (action) {

        case "new-chat":

            newChat();

            break;


        case "voice":

            toggleVoiceSubmenu();

            break;


        case "help":

            status(
                "Help / Quick Guide ابھی frontend placeholder ہے۔"
            );

            break;


        default:

            status(
                action
                    .replaceAll("-", " ") +
                " ابھی frontend placeholder ہے۔"
            );

            break;
    }
}


document.querySelectorAll(
    '#mainMenu [data-action]'
).forEach(button => {

    button.addEventListener(
        "click",
        event => {

            const action =
                button.dataset.action;


            if (
                action ===
                "voice"
            ) {

                return;
            }


            handleAction(action);
        }
    );
});


document.querySelectorAll(
    '#mainMenu .subitem[data-action]'
).forEach(button => {

    button.addEventListener(
        "click",
        () => {

            handleAction(
                button.dataset.action
            );

            closeMenu();
        }
    );
});


/* =========================================================
   MORE MENU ACTIONS
   ========================================================= */

document.querySelectorAll(
    '#moreMenu [data-action]'
).forEach(button => {

    button.addEventListener(
        "click",
        () => {

            handleAction(
                button.dataset.action
            );

            closeMoreMenu();
        }
    );
});


/*
=========================================================
   MORE MENU ACTIONS
   ========================================================= */

document.querySelectorAll(
    '#moreMenu [data-action]'
).forEach(button => {

    button.addEventListener(
        "click",
        () => {

            handleAction(
                button.dataset.action
            );

            closeMoreMenu();
        }
    );
});


/* =========================================================
   MENU SEARCH
   ========================================================= */

if (menuSearch) {

    menuSearch.addEventListener(
        "input",
        () => {

            const query =
                menuSearch.value
                    .trim()
                    .toLowerCase();


            mainMenu
                ?.querySelectorAll(
                    ".item, .subitem"
                )
                .forEach(item => {

                    const text =
                        item.textContent
                            .toLowerCase();


                    item.hidden =
                        Boolean(query) &&
                        !text.includes(query);
                });
        }
    );
}


/* =========================================================
   OUTSIDE CLICK
   ========================================================= */

document.addEventListener(
    "click",
    event => {

        const target =
            event.target;


        if (
            attachEl &&
            attachEl.classList.contains("open") &&
            !attachEl.contains(target) &&
            !plusButton?.contains(target)
        ) {

            closeAttachMenu();
        }


        if (
            mainMenu &&
            mainMenu.classList.contains("open") &&
            !mainMenu.contains(target) &&
            !menuButton?.contains(target)
        ) {

            closeMenu();
        }


        if (
            moreMenu &&
            moreMenu.classList.contains("open") &&
            !moreMenu.contains(target) &&
            !moreButton?.contains(target)
        ) {

            closeMoreMenu();
        }
    }
);


/* =========================================================
   ESCAPE KEY
   ========================================================= */

document.addEventListener(
    "keydown",
    event => {

        if (event.key !== "Escape") return;


        closeAttachMenu();

        closeMenu();

        closeMoreMenu();
    }
);


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
}


/* =========================================================
   PAGE VISIBILITY
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


        if (
            document.hidden &&
            live
        ) {

            stopLive();
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

setAttachState(false);

setMainMenuState(false);

setMoreMenuState(false);


if (voiceSubmenu) {

    voiceSubmenu.classList.remove(
        "open"
    );

    voiceSubmenu.setAttribute(
        "aria-hidden",
        "true"
    );
}


updateSend();

syncKeyboard();


/*
 * جان بوجھ کر یہاں کوئی automatic greeting
 * یا testing message نہیں رکھا گیا۔
 *
 * App کھلنے پر chat screen صاف رہے گی۔
 */
