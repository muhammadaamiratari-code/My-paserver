"""Simple in-process rate limiter.

For a multi-worker production deployment,
use Redis or another shared store.
"""

import time

from collections import defaultdict, deque
from threading import Lock


class RateLimiter:

    def __init__(
        self,
        max_requests: int,
        window_seconds: int
    ):

        self.max_requests = max_requests
        self.window_seconds = window_seconds

        self._events = defaultdict(deque)
        self._lock = Lock()

    def allow(self, key: str) -> bool:

        now = time.time()

        with self._lock:

            events = self._events[key]

            while (
                events
                and
                now - events[0]
                >= self.window_seconds
            ):
                events.popleft()

            if len(events) >= self.max_requests:
                return False

            events.append(now)

            return True

    def reset(self, key: str) -> None:

        with self._lock:
            self._events.pop(
                key,
                None
            )
