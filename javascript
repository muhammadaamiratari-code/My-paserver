/* ==========================================================================
   My AI Hub - Master JavaScript
   Assistant: MyPA

   Connected with:
   - index.html
   - style.css
   - server.py

   Main Functions:
   - Login
   - Registration
   - Session handling
   - Emergency Command
   - One-Time Emergency Code
   - Chat API
   - Smart Plus / Cancel
   - Voice UI
   - Live Call UI
   - Attachments
   - Settings
   - 3D Hub Review
   - Main Menu
   - New Chat
   - Logout
   - Mobile UI
   ========================================================================== */

"use strict";

document.addEventListener("DOMContentLoaded", () => {

    /* ======================================================================
       1. CONFIGURATION
       ====================================================================== */

    const API_BASE = "";

    const API = {
        login: `${API_BASE}/api/auth/login`,
        register: `${API_BASE}/api/auth/register`,
        logout: `${API_BASE}/api/auth/logout`,
        session: `${API_BASE}/api/auth/session`,

        chat: `${API_BASE}/api/chat`,

        emergencyVerify: `${API_BASE}/api/security/emergency/verify`,
        emergencyExecute: `${API_BASE}/api/security/emergency/execute`,

        biometricRegister: `${API_BASE}/api/auth/biometric/register`,
        biometricLogin: `${API_BASE}/api/auth/biometric/login`
    };


    /* ======================================================================
       2. APP STATE
       ====================================================================== */

    const AppState = {
        authenticated: false,
        user: null,

        isRecordingOrInCall: false,
        isListening: false,

        emergencyVerified: false,

        mediaStream: null,
        speechRecognition: null,

        selectedFile: null,

        currentRole: "USER"
    };


    /* ======================================================================
       3. ELEMENT HELPERS
       ====================================================================== */

    const $ = (id) => document.getElementById(id);


    const authScreen = $("authScreen");
    const appShell = $("appShell");

    const authMessage = $("authMessage");

    const loginPanel = $("loginPanel");
    const registerPanel = $("registerPanel");
    const emergencyPanel = $("emergencyPanel");

    const loginEmail = $("loginEmail");
    const loginPassword = $("loginPassword");
    const loginBtn = $("loginBtn");

    const registerEmail = $("registerEmail");
    const registerPassword = $("registerPassword");
    const registerPassword2 = $("registerPassword2");
    const registerBtn = $("registerBtn");

    const registerLink = $("registerLink");
    const backToLoginBtn = $("backToLoginBtn");

    const biometricLoginBtn = $("biometricLoginBtn");
    const biometricSetupBtn = $("biometricSetupBtn");

    const emergencyBtn = $("emergencyBtn");
    const closeEmergencyBtn = $("closeEmergencyBtn");

    const emergencyEmail = $("emergencyEmail");
    const emergencyPassword = $("emergencyPassword");
    const emergencyVerifyIdentityBtn =
        $("emergencyVerifyIdentityBtn");

    const emergencyCodeStep = $("emergencyCodeStep");
    const emergencyCode = $("emergencyCode");
    const emergencyCommand = $("emergencyCommand");
    const emergencyExecuteBtn = $("emergencyExecuteBtn");

    const mainMenuBtn = $("mainMenuBtn");
    const newChatBtn = $("newChatBtn");
    const moreBtn = $("moreBtn");

    const chatArea = $("chatArea");

    const waveVisualizer = $("wave");

    const plusButton = $("plusButton");
    const attachmentMenu = $("attachmentMenu");

    const messageInput = $("messageInput");
    const micButton = $("micButton");
    const liveButton = $("liveButton");
    const sendButton = $("sendButton");

    const filePicker = $("filePicker");
    const imagePicker = $("imagePicker");
    const cameraPicker = $("cameraPicker");

    const moreMenu = $("moreMenu");
    const moreBackdrop = $("moreBackdrop");

    const pageLayer = $("pageLayer");


    /* ======================================================================
       4. MESSAGE / NOTIFICATION SYSTEM
       ====================================================================== */

    function showAuthMessage(message, type = "error") {

        if (!authMessage) {
            return;
        }

        authMessage.textContent = message;

        authMessage.className =
            `auth-message show ${type}`;
    }


    function clearAuthMessage() {

        if (!authMessage) {
            return;
        }

        authMessage.textContent = "";

        authMessage.className = "auth-message";
    }


    function setButtonLoading(button, loading, text = "Processing...") {

        if (!button) {
            return;
        }

        if (loading) {

            button.dataset.originalText =
                button.textContent;

            button.textContent = text;

            button.disabled = true;

        } else {

            button.textContent =
                button.dataset.originalText ||
                button.textContent;

            button.disabled = false;
        }
    }


    /* ======================================================================
       5. API HELPER
       ====================================================================== */

    async function apiRequest(
        url,
        options = {}
    ) {

        const config = {
            method: "GET",
            credentials: "include",

            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {})
            },

            ...options
        };


        try {

            const response =
                await fetch(url, config);


            let data = {};

            try {
                data = await response.json();
            } catch {
                data = {};
            }


            if (!response.ok) {

                const error =
                    new Error(
                        data.message ||
                        data.error ||
                        "Server request failed."
                    );

                error.status = response.status;
                error.data = data;

                throw error;
            }


            return data;

        } catch (error) {

            console.error(
                "API Error:",
                error
            );

            throw error;
        }
    }


    /* ======================================================================
       6. AUTH SCREEN MANAGEMENT
       ====================================================================== */

    function showLoginPanel() {

        if (loginPanel) {
            loginPanel.classList.remove("hidden");
        }

        if (registerPanel) {
            registerPanel.classList.add("hidden");
        }

        if (emergencyPanel) {
            emergencyPanel.classList.add("hidden");
        }

        clearAuthMessage();
    }


    function showRegisterPanel() {

        if (loginPanel) {
            loginPanel.classList.add("hidden");
        }

        if (registerPanel) {
            registerPanel.classList.remove("hidden");
        }

        if (emergencyPanel) {
            emergencyPanel.classList.add("hidden");
        }

        clearAuthMessage();
    }


    function showEmergencyPanel() {

        if (loginPanel) {
            loginPanel.classList.add("hidden");
        }

        if (registerPanel) {
            registerPanel.classList.add("hidden");
        }

        if (emergencyPanel) {
            emergencyPanel.classList.remove("hidden");
        }

        if (emergencyCodeStep) {
            emergencyCodeStep.classList.add("hidden");
        }

        clearAuthMessage();
    }


    function showMainApp() {

        if (authScreen) {
            authScreen.classList.add("hidden");
        }

        if (appShell) {
            appShell.classList.remove("hidden");
        }

        AppState.authenticated = true;

        messageInput?.focus();
    }


    function showAuthScreen() {

        if (appShell) {
            appShell.classList.add("hidden");
        }

        if (authScreen) {
            authScreen.classList.remove("hidden");
        }

        AppState.authenticated = false;
        AppState.user = null;

        showLoginPanel();
    }


    /* ======================================================================
       7. LOGIN
       ====================================================================== */

    async function loginUser() {

        const email =
            loginEmail?.value.trim();

        const password =
            loginPassword?.value || "";


        if (!email) {

            showAuthMessage(
                "براہِ کرم Gmail درج کریں۔",
                "error"
            );

            return;
        }


        if (!password) {

            showAuthMessage(
                "براہِ کرم Password درج کریں۔",
                "error"
            );

            return;
        }


        setButtonLoading(
            loginBtn,
            true,
            "Login ہو رہا ہے..."
        );


        try {

            const data =
                await apiRequest(
                    API.login,
                    {
                        method: "POST",

                        body: JSON.stringify({
                            email,
                            password
                        })
                    }
                );


            AppState.user =
                data.user || null;

            AppState.currentRole =
                data.role || "USER";


            showMainApp();

            addSystemMessage(
                "MyPA تیار ہے۔"
            );


        } catch (error) {

            showAuthMessage(
                error.message ||
                "Login ناکام ہوگیا۔",
                "error"
            );

        } finally {

            setButtonLoading(
                loginBtn,
                false
            );
        }
    }


    /* ======================================================================
       8. REGISTRATION
       ====================================================================== */

    async function registerUser() {

        const email =
            registerEmail?.value.trim();

        const password =
            registerPassword?.value || "";

        const password2 =
            registerPassword2?.value || "";


        if (!email) {

            showAuthMessage(
                "Gmail درج کریں۔",
                "error"
            );

            return;
        }


        if (password.length < 8) {

            showAuthMessage(
                "Password کم از کم 8 حروف کا ہونا چاہیے۔",
                "error"
            );

            return;
        }


        if (password !== password2) {

            showAuthMessage(
                "دونوں Password ایک جیسے نہیں ہیں۔",
                "error"
            );

            return;
        }


        setButtonLoading(
            registerBtn,
            true,
            "اکاؤنٹ بنایا جا رہا ہے..."
        );


        try {

            const data =
                await apiRequest(
                    API.register,
                    {
                        method: "POST",

                        body: JSON.stringify({
                            email,
                            password
                        })
                    }
                );


            showAuthMessage(
                data.message ||
                "اکاؤنٹ بن گیا ہے۔ اب Login کریں۔",
                "success"
            );


            registerPassword.value = "";
            registerPassword2.value = "";

            setTimeout(() => {
                showLoginPanel();
            }, 800);


        } catch (error) {

            showAuthMessage(
                error.message ||
                "اکاؤنٹ نہیں بن سکا۔",
                "error"
            );

        } finally {

            setButtonLoading(
                registerBtn,
                false
            );
        }
    }


    /* ======================================================================
       9. SESSION CHECK
       ====================================================================== */

    async function checkExistingSession() {

        try {

            const data =
                await apiRequest(
                    API.session
                );


            if (data.authenticated) {

                AppState.authenticated = true;

                AppState.user =
                    data.user || null;

                AppState.currentRole =
                    data.role || "USER";

                showMainApp();

            } else {

                showAuthScreen();
            }

        } catch {

            showAuthScreen();
        }
    }


    /* ======================================================================
       10. LOGOUT
       ====================================================================== */

    async function logoutUser() {

        try {

            await apiRequest(
                API.logout,
                {
                    method: "POST"
                }
            );

        } catch (error) {

            console.warn(
                "Logout request failed:",
                error
            );

        } finally {

            stopVoiceOrCall();

            AppState.authenticated = false;
            AppState.user = null;

            closeMoreMenu();

            showAuthScreen();
        }
    }


    /* ======================================================================
       11. CHAT MESSAGE UI
       ====================================================================== */

    function addMessage(
        text,
        type = "ai"
    ) {

        if (!chatArea) {
            return null;
        }


        const message =
            document.createElement("div");


        message.classList.add(
            "message"
        );


        if (type === "user") {

            message.classList.add(
                "user-message"
            );

        } else {

            message.classList.add(
                "ai-message"
            );
        }


        message.textContent = text;

        chatArea.appendChild(message);

        chatArea.scrollTop =
            chatArea.scrollHeight;


        return message;
    }


    function addSystemMessage(text) {

        if (!chatArea) {
            return;
        }


        const message =
            document.createElement("div");


        message.className =
            "system-message";


        message.textContent = text;


        chatArea.appendChild(message);

        chatArea.scrollTop =
            chatArea.scrollHeight;
    }


    function clearChat() {

        if (!chatArea) {
            return;
        }

        chatArea.innerHTML = "";
    }


    /* ======================================================================
       12. SEND MESSAGE TO SERVER
       ====================================================================== */

    async function sendMessageToServer(
        text
    ) {

        const cleanText =
            String(text || "").trim();


        if (!cleanText) {
            return;
        }


        if (!AppState.authenticated) {

            showAuthScreen();

            return;
        }


        addMessage(
            cleanText,
            "user"
        );


        const loadingMessage =
            addMessage(
                "MyPA جواب تیار کر رہا ہے...",
                "ai"
            );


        try {

            const data =
                await apiRequest(
                    API.chat,
                    {
                        method: "POST",

                        body: JSON.stringify({
                            message: cleanText
                        })
                    }
                );


            if (loadingMessage) {

                loadingMessage.textContent =
                    data.reply ||
                    "مجھے سرور سے جواب نہیں ملا۔";
            }


        } catch (error) {

            if (loadingMessage) {

                loadingMessage.textContent =
                    error.message ||
                    "سرور سے رابطہ نہیں ہوسکا۔";
            }
        }
    }


    function sendCurrentMessage() {

        if (!messageInput) {
            return;
        }


        const text =
            messageInput.value.trim();


        if (!text) {
            return;
        }


        messageInput.value = "";

        sendMessageToServer(text);
    }


    /* ======================================================================
       13. ENTER TO SEND
       ====================================================================== */

    function handleInputKeydown(event) {

        if (event.key !== "Enter") {
            return;
        }


        if (event.shiftKey) {
            return;
        }


        event.preventDefault();

        sendCurrentMessage();
    }


    /* ======================================================================
       14. SMART PLUS / CANCEL
       ====================================================================== */

    function setCancelMode(active) {

        AppState.isRecordingOrInCall =
            active;


        if (!plusButton) {
            return;
        }


        if (active) {

            plusButton.classList.add(
                "cancel-mode"
            );

            plusButton.textContent = "✕";

            plusButton.setAttribute(
                "aria-label",
                "Cancel Recording/Call"
            );


            attachmentMenu?.classList.remove(
                "active"
            );

        } else {

            plusButton.classList.remove(
                "cancel-mode"
            );

            plusButton.textContent = "+";

            plusButton.setAttribute(
                "aria-label",
                "Open Attachments"
            );
        }
    }


    function stopVoiceOrCall() {

        stopVoiceRecognition();

        stopMediaStream();


        setCancelMode(false);


        waveVisualizer?.classList.remove(
            "active"
        );


        if (messageInput) {

            messageInput.placeholder =
                "کوئی پیغام لکھیں...";
        }
    }


    /* ======================================================================
       15. MICROPHONE / SPEECH RECOGNITION
       ====================================================================== */

    function createSpeechRecognition() {

        const Recognition =
            window.SpeechRecognition ||
            window.webkitSpeechRecognition;


        if (!Recognition) {
            return null;
        }


        const recognition =
            new Recognition();


        recognition.lang = "ur-PK";

        recognition.continuous = false;

        recognition.interimResults = true;


        recognition.onstart = () => {

            AppState.isListening = true;

            setCancelMode(true);

            waveVisualizer?.classList.add(
                "active"
            );


            if (messageInput) {

                messageInput.placeholder =
                    "مائیک آن ہے، بولیں...";
            }
        };


        recognition.onresult = (event) => {

            let finalText = "";

            for (
                let i = event.resultIndex;
                i < event.results.length;
                i++
            ) {

                finalText +=
                    event.results[i][0].transcript;
            }


            if (messageInput) {

                messageInput.value =
                    finalText;
            }
        };


        recognition.onerror = (event) => {

            console.warn(
                               "Speech start error:",
                error
            );
        }
    }


    function stopVoiceRecognition() {

        if (!AppState.speechRecognition) {
            return;
        }


        try {

            AppState.speechRecognition.stop();

        } catch {
            // Already stopped.
        }


        AppState.isListening = false;
    }


    /* ======================================================================
       16. LIVE CALL
       ====================================================================== */

    async function startLiveCall() {

        setCancelMode(true);

        waveVisualizer?.classList.add(
            "active"
        );


        if (messageInput) {

            messageInput.placeholder =
                "لائیو کال جاری ہے...";
        }


        if (
            !navigator.mediaDevices ||
            !navigator.mediaDevices.getUserMedia
        ) {

            addSystemMessage(
                "اس device/browser میں Live Audio API دستیاب نہیں۔"
            );

            return;
        }


        try {

            const stream =
                await navigator.mediaDevices.getUserMedia({
                    audio: true
                });


            AppState.mediaStream =
                stream;


            addSystemMessage(
                "Live Audio permission فعال ہے۔"
            );

        } catch (error) {

            console.warn(
                "Microphone permission:",
                error
            );


            addSystemMessage(
                "Microphone کی اجازت نہیں ملی۔"
            );

            setCancelMode(false);

            waveVisualizer?.classList.remove(
                "active"
            );
        }
    }


    function stopMediaStream() {

        if (!AppState.mediaStream) {
            return;
        }


        AppState.mediaStream
            .getTracks()
            .forEach(
                track => track.stop()
            );


        AppState.mediaStream = null;
    }


    /* ======================================================================
       17. ATTACHMENT MENU
       ====================================================================== */

    function toggleAttachmentMenu() {

        if (!attachmentMenu) {
            return;
        }


        attachmentMenu.classList.toggle(
            "active"
        );


        closeMoreMenu();
    }


    function closeAttachmentMenu() {

        attachmentMenu?.classList.remove(
            "active"
        );
    }


    function handleAttachment(type) {

        closeAttachmentMenu();


        if (type === "file") {

            filePicker?.click();

        } else if (type === "image") {

            imagePicker?.click();

        } else if (type === "camera") {

            cameraPicker?.click();
        }
    }


    function handleSelectedFile(file) {

        if (!file) {
            return;
        }


        AppState.selectedFile =
            file;


        addSystemMessage(
            `فائل منتخب ہوگئی: ${file.name}`
        );


        /*
         * اصل file upload API بعد میں server.py میں
         * secure endpoint کے ذریعے شامل کیا جا سکتا ہے۔
         *
         * فی الحال browser-side selection محفوظ رکھی گئی ہے۔
         */
    }


    /* ======================================================================
       18. THREE DOT MENU
       ====================================================================== */

    function openMoreMenu() {

        moreMenu?.classList.add(
            "open"
        );

        moreBackdrop?.classList.add(
            "active"
        );
    }


    function closeMoreMenu() {

        moreMenu?.classList.remove(
            "open"
        );

        moreBackdrop?.classList.remove(
            "active"
        );
    }


    function toggleMoreMenu() {

        if (
            moreMenu?.classList.contains(
                "open"
            )
        ) {

            closeMoreMenu();

        } else {

            openMoreMenu();
        }
    }


    /* ======================================================================
       19. SETTINGS MODAL
       ====================================================================== */

    window.openSettingsModal =
        function openSettingsModal() {

            if (!pageLayer) {
                return;
            }


            pageLayer.innerHTML = `
                <div class="modal-content">

                    <h3>
                        App Settings & Security
                    </h3>

                    <hr>


                    <div class="settings-row">

                        <div>
                            <strong>
                                Auto Sync & Cloud Backup
                            </strong>

                            <small>
                                خودکار مقامی Backup
                            </small>
                        </div>

                        <input
                            type="checkbox"
                            id="autoSyncToggle"
                        >

                    </div>


                    <hr>


                    <div class="settings-row">

                        <div>
                            <strong>
                                Offline Voice Mode
                            </strong>

                            <small>
                                دستیاب ہونے پر Offline Speech Recognition
                            </small>
                        </div>

                        <input
                            type="checkbox"
                            id="offlineVoiceToggle"
                            checked
                        >

                    </div>


                    <hr>


                    <div class="settings-row">

                        <div>
                            <strong>
                                Data Saver Mode
                            </strong>

                            <small>
                                غیر ضروری Data استعمال کم کریں
                            </small>
                        </div>

                        <input
                            type="checkbox"
                            id="dataSaverToggle"
                        >

                    </div>


                    <hr>


                    <div class="settings-row">

                        <div>
                            <strong>
                                Emergency Protection
                            </strong>

                            <small>
                                Emergency Commands server-side verify ہوں گی۔
                            </small>
                        </div>

                        <input
                            type="checkbox"
                            id="emergencyProtectionToggle"
                            checked
                            disabled
                        >

                    </div>


                    <button
                        id="closeSettingsBtn"
                        class="primary-btn"
                        type="button"
                    >
                        محفوظ کریں اور بند کریں
                    </button>

                </div>
            `;


            pageLayer.classList.add(
                "active"
            );


            const autoSync =
                $("autoSyncToggle");

            const offlineVoice =
                $("offlineVoiceToggle");

            const dataSaver =
                $("dataSaverToggle");


            autoSync.checked =
                localStorage.getItem(
                    "mypa_auto_sync"
                ) === "true";


            dataSaver.checked =
                localStorage.getItem(
                    "mypa_data_saver"
                ) === "true";


            $("closeSettingsBtn")
                ?.addEventListener(
                    "click",
                    () => {

                        localStorage.setItem(
                            "mypa_auto_sync",
                            String(autoSync.checked)
                        );


                        localStorage.setItem(
                            "mypa_data_saver",
                            String(dataSaver.checked)
                        );


                        localStorage.setItem(
                            "mypa_offline_voice",
                            String(offlineVoice.checked)
                        );


                        pageLayer.classList.remove(
                            "active"
                        );
                    }
                );
        };


    /* ======================================================================
       20. 3D HUB REVIEW
       ====================================================================== */

    window.open3DHubReview =
        function open3DHubReview() {

            if (!pageLayer) {
                return;
            }


            pageLayer.innerHTML = `
                <div class="modal-content">

                    <h3>
                        Live 3D Hub Review
                    </h3>


                    <div
                        class="canvas-3d-container"
                    >

                        <div
                            class="personal-assistant-room"
                        >

                            <small
                                style="
                                color:#00a86b;
                                font-size:10px;
                                "
                            >
                                Private MyPA Room
                            </small>


                            <div
                                id="personalAssistantAvatar"
                                class="assistant-avatar"
                            >

                                <div
                                    class="assistant-avatar-circle"
                                >
                                    MyPA
                                </div>


                                <span
                                    id="assistantStatusText"
                                    class="assistant-status"
                                >
                                    Desk پر Ready
                                </span>

                            </div>

                        </div>


                        <div
                            class="outer-hall"
                        >

                            <small
                                style="
                                color:#ab47bc;
                                font-size:10px;
                                "
                            >
                                Outer Hall: 10 Agents & Safe Shelf
                            </small>


                            <div
                                class="library-shelf"
                            >
                                Library Shelf
                            </div>

                        </div>

                    </div>


                    <div
                        class="modal-button-row"
                    >

                        <button
                            id="callAssistantBtn"
                            type="button"
                            style="
                            background:#00a86b;
                            color:#000;
                            "
                        >
                            Live Call Test
                        </button>


                        <button
                            id="fetchFromFolderBtn"
                            type="button"
                            style="
                            background:#ff9800;
                            color:#fff;
                            "
                        >
                            Command: Fetch Safe File
                        </button>

                    </div>


                    <button
                        id="close3DModalBtn"
                        class="modal-close-btn"
                        type="button"
                    >
                        بند کریں
                    </button>

                </div>
            `;


            pageLayer.classList.add(
                "active"
            );


            const avatar =
                $("personalAssistantAvatar");

            const statusText =
                $("assistantStatusText");


            $("callAssistantBtn")
                ?.addEventListener(
                    "click",
                    () => {

                        statusText.textContent =
                            "Call Active: Baat Sun Raha Hai...";

                        avatar.style.transform =
                            "scale(1.15)";
                    }
                );


            $("fetchFromFolderBtn")
                ?.addEventListener(
                    "click",
                    () => {

                        statusText.textContent =
                            "Library Shelf Taraf Ja Raha Hai...";

                        avatar.style.transform =
                            "translate(150px, 20px)";


                        setTimeout(
                            () => {

                                statusText.textContent =
                                    "File واپس Desk پر آ گئی۔";

                                avatar.style.transform =
                                    "translate(0, 0)";

                            },
                            2500
                        );
                    }
                );


            $("close3DModalBtn")
                ?.addEventListener(
                    "click",
                    () => {

                        pageLayer.classList.remove(
                            "active"
                        );
                    }
                );
        };


    /* ======================================================================
       21. EMERGENCY IDENTITY VERIFICATION
       ====================================================================== */

    async function verifyEmergencyIdentity() {

        const email =
            emergencyEmail?.value.trim();

        const password =
            emergencyPassword?.value || "";


        if (!email || !password) {

            showAuthMessage(
                "Emergency کے لیے Gmail اور Password دونوں ضروری ہیں۔",
                "error"
            );

            return;
        }


        setButtonLoading(
            emergencyVerifyIdentityBtn,
            true,
            "شناخت چیک ہو رہی ہے..."
        );


        try {

            const data =
                await apiRequest(
                    API.emergencyVerify,
                    {
                        method: "POST",

                        body: JSON.stringify({
                            email,
                            password
                        })
                    }
                );


            AppState.emergencyVerified =
                true;


            emergencyCodeStep
                ?.classList.remove(
                    "hidden"
                );


            showAuthMessage(
                data.message ||
                "شناخت verify ہوگئی۔ اب One-Time Code درج کریں۔",
                "success"
            );


        } catch (error) {

            AppState.emergencyVerified =
                false;


            showAuthMessage(
                error.message ||
                "Emergency identity verification ناکام ہوگئی۔",
                "error"
            );

        } finally {

            setButtonLoading(
                emergencyVerifyIdentityBtn,
                false
            );
        }
    }


    /* ======================================================================
       22. EMERGENCY COMMAND EXECUTION
       ====================================================================== */

    async function executeEmergencyCommand() {

        if (!AppState.emergencyVerified) {

            showAuthMessage(
                "پہلے شناخت verify کریں۔",
                "error"
            );

            return;
        }


        const code =
            emergencyCode?.value.trim();

        const command =
            emergencyCommand?.value.trim();


        if (!code) {

            showAuthMessage(
                "One-Time Emergency Code درج کریں۔",
                "error"
            );

            return;
        }


        if (!command) {

            showAuthMessage(
                "Emergency Command درج کریں۔",
                "error"
            );

            return;
        }


        setButtonLoading(
            emergencyExecuteBtn,
            true,
            "Command verify ہو رہی ہے..."
        );


        try {

            const data =
                await apiRequest(
                    API.emergencyExecute,
                    {
                        method: "POST",

                        body: JSON.stringify({
                            otp_code: code,
                            command: command
                        })
                    }
                );


            showAuthMessage(
                data.message ||
                "Emergency Command مکمل ہوگئی۔",
                "success"
            );


            /*
             * اہم:
             * Frontend کو Safe Folder کا اصل حساس data
             * براہ راست نہیں دیا جا رہا۔
             *
             * Server کو چاہیے کہ authorization،
             * code burning اور protected resource
             * access server-side handle کرے۔
             */


            if (data.status === "success") {

                emergencyCode.value = "";

                emergencyCommand.value = "";

                AppState.emergencyVerified =
                    false;

                emergencyCodeStep
                    ?.classList.add(
                        "hidden"
                    );
            }


        } catch (error) {

            showAuthMessage(
                error.message ||
                "Emergency Command مکمل نہیں ہوسکی۔",
                "error"
            );

        } finally {

            setButtonLoading(
                emergencyExecuteBtn,
                false
            );
        }
    }


    /* ======================================================================
       23. BIOMETRIC / PASSKEY SETUP
       ====================================================================== */

    async function setupBiometric() {

        /*
         * حقیقی Face/Fingerprint verification کے لیے
         * WebAuthn / Passkey استعمال ہونا چاہیے۔
         *
         * Browser خود biometric verification کرتا ہے۔
         * JavaScript کو اصل fingerprint یا face image نہیں ملتی۔
         */


        if (
            !window.PublicKeyCredential
        ) {

            showAuthMessage(
                "اس browser/device میں Passkey یا WebAuthn دستیاب نہیں۔",
                "warning"
            );

            return;
        }


        showAuthMessage(
            "Biometric / Passkey setup کے لیے server-side WebAuthn registration ضروری ہے۔",
            "warning"
        );


        /*
         * Production implementation میں یہاں server سے
         * PublicKeyCredentialCreationOptions لائے جائیں گے،
         * پھر navigator.credentials.create()
         * استعمال ہوگا۔
         */
    }


    async function biometricLogin() {

        if (
            !window.PublicKeyCredential
        ) {

            showAuthMessage(
                "اس browser/device میں Passkey دستیاب نہیں۔",
                "warning"
            );

            return;
        }


        showAuthMessage(
            "Biometric / Passkey login 
           کے لیے server-side WebAuthn registration ضروری ہے۔",
            "warning"
        );


        /*
         * Production implementation میں یہاں server سے
         * PublicKeyCredentialCreationOptions لائے جائیں گے،
         * پھر navigator.credentials.create()
         * استعمال ہوگا۔
         */
    }


    async function biometricLogin() {

        if (
            !window.PublicKeyCredential
        ) {

            showAuthMessage(
                "اس browser/device میں Passkey دستیاب نہیں۔",
                "warning"
            );

            return;
        }


        showAuthMessage(
            "Biometric / Passkey login کے لیے server-side WebAuthn challenge ضروری ہے۔",
            "warning"
        );


        /*
         * Production WebAuthn flow:
         *
         * 1. Server challenge دے گا
         * 2. navigator.credentials.get()
         * 3. Credential server کو واپس
         * 4. Server verification
         * 5. Session creation
         */
    }


    /* ======================================================================
       24. NEW CHAT
       ====================================================================== */

    function startNewChat() {

        clearChat();

        addSystemMessage(
            "نئی گفتگو شروع ہوگئی۔"
        );

        messageInput?.focus();
    }


    /* ======================================================================
       25. MAIN MENU
       ====================================================================== */

    function openMainMenu() {

        addSystemMessage(
            "Main Menu: Settings، Security، Emergency اور Logout آپشنز Three-dot menu میں دستیاب ہیں۔"
        );

        openMoreMenu();
    }


    /* ======================================================================
       26. PAGE LAYER CLOSE
       ====================================================================== */

    function closePageLayer() {

        if (!pageLayer) {
            return;
        }

        pageLayer.classList.remove(
            "active"
        );

        pageLayer.innerHTML = "";
    }


    /* ======================================================================
       27. GLOBAL CLICK HANDLERS
       ====================================================================== */

    if (registerLink) {

        registerLink.addEventListener(
            "click",
            showRegisterPanel
        );
    }


    if (backToLoginBtn) {

        backToLoginBtn.addEventListener(
            "click",
            showLoginPanel
        );
    }


    if (loginBtn) {

        loginBtn.addEventListener(
            "click",
            loginUser
        );
    }


    if (registerBtn) {

        registerBtn.addEventListener(
            "click",
            registerUser
        );
    }


    if (emergencyBtn) {

        emergencyBtn.addEventListener(
            "click",
            showEmergencyPanel
        );
    }


    if (closeEmergencyBtn) {

        closeEmergencyBtn.addEventListener(
            "click",
            showLoginPanel
        );
    }


    if (emergencyVerifyIdentityBtn) {

        emergencyVerifyIdentityBtn.addEventListener(
            "click",
            verifyEmergencyIdentity
        );
    }


    if (emergencyExecuteBtn) {

        emergencyExecuteBtn.addEventListener(
            "click",
            executeEmergencyCommand
        );
    }


    if (biometricLoginBtn) {

        biometricLoginBtn.addEventListener(
            "click",
            biometricLogin
        );
    }


    if (biometricSetupBtn) {

        biometricSetupBtn.addEventListener(
            "click",
            setupBiometric
        );
    }


    if (sendButton) {

        sendButton.addEventListener(
            "click",
            sendCurrentMessage
        );
    }


    if (messageInput) {

        messageInput.addEventListener(
            "keydown",
            handleInputKeydown
        );
    }


    if (plusButton) {

        plusButton.addEventListener(
            "click",
            () => {

                if (
                    AppState.isRecordingOrInCall
                ) {

                    stopVoiceOrCall();

                } else {

                    toggleAttachmentMenu();
                }
            }
        );
    }


    if (micButton) {

        micButton.addEventListener(
            "click",
            () => {

                if (
                    AppState.isListening
                ) {

                    stopVoiceOrCall();

                } else {

                    startVoiceRecognition();
                }
            }
        );
    }


    if (liveButton) {

        liveButton.addEventListener(
            "click",
            () => {

                if (
                    AppState.isRecordingOrInCall
                ) {

                    stopVoiceOrCall();

                } else {

                    startLiveCall();
                }
            }
        );
    }


    if (moreBtn) {

        moreBtn.addEventListener(
            "click",
            toggleMoreMenu
        );
    }


    if (moreBackdrop) {

        moreBackdrop.addEventListener(
            "click",
            closeMoreMenu
        );
    }


    if (mainMenuBtn) {

        mainMenuBtn.addEventListener(
            "click",
            openMainMenu
        );
    }


    if (newChatBtn) {

        newChatBtn.addEventListener(
            "click",
            startNewChat
        );
    }


    /* ======================================================================
       28. ATTACHMENT EVENT DELEGATION
       ====================================================================== */

    if (attachmentMenu) {

        attachmentMenu.addEventListener(
            "click",
            (event) => {

                const button =
                    event.target.closest(
                        "button"
                    );


                if (!button) {
                    return;
                }


                const type =
                    button.dataset.attachment;


                handleAttachment(type);
            }
        );
    }


    if (filePicker) {

        filePicker.addEventListener(
            "change",
            () => {

                handleSelectedFile(
                    filePicker.files?.[0]
                );

                filePicker.value = "";
            }
        );
    }


    if (imagePicker) {

        imagePicker.addEventListener(
            "change",
            () => {

                handleSelectedFile(
                    imagePicker.files?.[0]
                );

                imagePicker.value = "";
            }
        );
    }


    if (cameraPicker) {

        cameraPicker.addEventListener(
            "change",
            () => {

                handleSelectedFile(
                    cameraPicker.files?.[0]
                );

                cameraPicker.value = "";
            }
        );
    }


    /* ======================================================================
       29. THREE-DOT MENU EVENT DELEGATION
       ====================================================================== */

    if (moreMenu) {

        moreMenu.addEventListener(
            "click",
            (event) => {

                const button =
                    event.target.closest(
                        ".more-item"
                    );


                if (!button) {
                    return;
                }


                const action =
                    button.dataset.action;


                if (action === "settings") {

                    closeMoreMenu();

                    window.openSettingsModal();


                } else if (
                    action === "live-3d-review"
                ) {

                    closeMoreMenu();

                    window.open3DHubReview();


                } else if (
                    action === "emergency"
                ) {

                    closeMoreMenu();

                    showEmergencyPanel();

                    if (authScreen) {
                        authScreen.classList.remove(
                            "hidden"
                        );
                    }

                    if (appShell) {
                        appShell.classList.add(
                            "hidden"
                        );
                    }


                } else if (
                    action === "logout"
                ) {

                    closeMoreMenu();

                    logoutUser();
                }
            }
        );
    }


    /* ======================================================================
       30. ESCAPE KEY
       ====================================================================== */

    document.addEventListener(
        "keydown",
        (event) => {

            if (event.key !== "Escape") {
                return;
            }


            closeMoreMenu();

            closeAttachmentMenu();

            closePageLayer();


            if (
                AppState.isRecordingOrInCall
            ) {

                stopVoiceOrCall();
            }
        }
    );


    /* ======================================================================
       31. INITIALIZATION
       ====================================================================== */

    async function initializeApp() {

        /*
         * شروع میں login/session check۔
         */

        await checkExistingSession();
    }


    initializeApp();

});
        
