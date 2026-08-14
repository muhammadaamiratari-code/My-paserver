/* My AI Hub - Authentication Module */

(function () {
  "use strict";

  const KEY = "mya_hub_auth_v1";

  const Auth = {
    state: {
      authenticated: false,
      role: "USER",
      email: null,
      setupComplete: false
    },

    load() {
      try {
        this.state = {
          ...this.state,
          ...JSON.parse(localStorage.getItem(KEY) || "{}")
        };
      } catch (_) {}
      return this.state;
    },

    save() {
      localStorage.setItem(KEY, JSON.stringify(this.state));
    },

    registerUser(email, password) {
      if (!this.validEmail(email)) {
        return {
          ok: false,
          message: "Please enter a valid email address."
        };
      }

      if (!password || password.length < 8) {
        return {
          ok: false,
          message: "Password must contain at least 8 characters."
        };
      }

      this.state.email = email.trim().toLowerCase();
      this.state.authenticated = true;
      this.state.role = "USER";
      this.state.setupComplete = true;

      this.save();

      return {
        ok: true,
        message: "Account setup completed."
      };
    },

    loginUser(email) {
      if (!this.validEmail(email)) {
        return {
          ok: false,
          message: "Please enter a valid email address."
        };
      }

      if (
        !this.state.email ||
        this.state.email !== email.trim().toLowerCase()
      ) {
        return {
          ok: false,
          message: "This account was not found. Please check your email address."
        };
      }

      this.state.authenticated = true;
      this.save();

      return {
        ok: true,
        message: "Login successful."
      };
    },

    authenticateOwner() {
      return {
        ok: false,
        message: "Owner authentication must be verified by the secure backend."
      };
    },

    logout() {
      this.state.authenticated = false;
      this.state.role = "USER";
      this.save();
    },

    isOwner() {
      return (
        this.state.authenticated &&
        this.state.role === "OWNER"
      );
    },

    isAuthenticated() {
      return Boolean(this.state.authenticated);
    },

    validEmail(email) {
      return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(
        String(email || "")
      );
    }
  };

  Auth.load();

  window.MyPAAuth = Auth;

})();
