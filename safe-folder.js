/* My AI Hub - Safe Folder Client Controller
   Protected files must be authorized and stored server-side.
*/

(function () {

  "use strict";

  const SafeFolder = {

    async requestFile({
      appCode,
      otpCode,
      fileId
    }) {

      if (
        !appCode ||
        !otpCode ||
        !fileId
      ) {

        return {
          ok: false,
          message:
            "App code, one-time code and file ID are required."
        };
      }

      try {

        const response = await fetch(
          "/api/safe-folder/request",
          {
            method: "POST",

            headers: {
              "Content-Type": "application/json"
            },

            body: JSON.stringify({
              app_code: appCode,
              otp_code: otpCode,
              file_id: fileId
            })
          }
        );

        const data =
          await response.json().catch(
            () => ({})
          );

        if (!response.ok) {

          return {
            ok: false,
            message:
              data.message ||
              "Safe Folder access was denied."
          };
        }

        return {
          ok: true,
          data
        };

      } catch (_) {

        return {
          ok: false,
          message:
            "The Safe Folder service could not be reached."
        };
      }
    },

    clearLocalSession() {

      sessionStorage.removeItem(
        "mypa_safe_folder_session"
      );
    }
  };

  window.MyPASafeFolder = SafeFolder;

})();
