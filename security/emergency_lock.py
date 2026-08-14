"""Server-side emergency lockdown state.

For multi-worker production deployments,
store this state in Redis or a database.
"""

from threading import Lock


class EmergencyLock:

    def __init__(self):

        self._active = False
        self._reason = "manual"

        self._lock = Lock()

    def activate(
        self,
        reason: str = "security_event"
    ):

        with self._lock:

            self._active = True
            self._reason = reason

    def deactivate(self):

        with self._lock:

            self._active = False
            self._reason = "none"

    def is_active(self) -> bool:

        with self._lock:
            return self._active

    def reason(self) -> str:

        with self._lock:
            return self._reason


EMERGENCY_LOCK = EmergencyLock()
