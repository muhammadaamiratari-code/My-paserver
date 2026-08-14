/* My AI Hub - AI Router
   Gemini Primary
   OpenAI Fallback
   API keys remain on the server.
*/

(function () {

  "use strict";

  const Router = {

    async ask(message, authToken) {

      if (!String(message || "").trim()) {

        return {
          ok: false,
          message: "Please enter a message."
        };
      }

      try {

        const response = await fetch(
          "/api/chat",
          {
            method: "POST",

            headers: {
              "Content-Type": "application/json"
            },

            body: JSON.stringify({
              message:
                String(message).trim(),

              auth_token:
                authToken ||
                "STANDARD_USER"
            })
          }
        );

        const data =
          await response.json().catch(
            () => ({})
          );

        if (!response.ok) {
          throw new Error(
            data.message ||
            "AI request failed."
          );
        }

        return {

          ok: true,

          provider:
            data.provider ||
            "gemini",

          reply:
            data.reply || ""
        };

      } catch (_) {

        return {

          ok: false,

          message:
            "Our AI service is temporarily unavailable. Please try again shortly."
        };
      }
    }
  };

  window.MyPAAIRouter = Router;

})();
