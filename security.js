/* My AI Hub - Security Module */

(function () {
  "use strict";

  const KEY = "mya_security_state_v1";
  const MAX_ATTEMPTS = 5;
  const LOCK_MS = 15 * 60 * 1000;

  const Security = {

    state: {
      failedAttempts: 0,
      lockedUntil: 0,
      emergencyActive: false
    },

    load() {
      try {
        this.state = {
          ...this.state,
          ...JSON.parse(
            localStorage.getItem(KEY) || "{}"
          )
        };
      } catch (_) {}
    },

    save() {
      localStorage.setItem(
        KEY,
        JSON.stringify(this.state)
      );
    },

    isLocked() {

      if (Date.now() >= this.state.lockedUntil) {

        this.state.failedAttempts = 0;
        this.state.lockedUntil = 0;

        this.save();

        return false;
      }

      return true;
    },

    registerFailure() {

      this.state.failedAttempts++;

      if (
        this.state.failedAttempts >= MAX_ATTEMPTS
      ) {
        this.state.lockedUntil =
          Date.now() + LOCK_MS;
      }

      this.save();
    },

    async verifyRemoteCommand(
      appCode,
      otpCode,
      commandType
    ) {

      if (this.isLocked()) {

        return {
          ok: false,
          message:
            "Security access is temporarily locked. Please try again later."
        };
      }

      try {

        const response = await fetch(
          "/api/security/remote-command",
          {
            method: "POST",

            headers: {
              "Content-Type": "application/json"
            },

            body: JSON.stringify({
              app_code: appCode,
              otp_code: otpCode,
              command_type: commandType
            })
          }
        );

        const data =
          await response.json().catch(
            () => ({})
          );

        if (!response.ok) {

          this.registerFailure();

          return {
            ok: false,
            message:
              data.message ||
              data.reason ||
              "Security verification failed."
          };
        }

        this.state.failedAttempts = 0;

        this.save();

        return {
          ok: true,
          data
        };

      } catch (_) {

        return {
          ok: false,
          message:
            "Network connection failed. Please try again."
        };
      }
    },

    activateEmergency() {

      this.state.emergencyActive = true;

      this.save();
    },

    clearEmergency() {

      this.state.emergencyActive = false;

      this.save();
    }
  };

  Security.load();

  window.MyPASecurity = Security;

})();
