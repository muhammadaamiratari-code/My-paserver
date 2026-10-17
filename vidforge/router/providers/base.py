"""Common interface for pluggable VidForge video engines."""

from abc import ABC, abstractmethod


class VideoEngine(ABC):
    @abstractmethod
    def submit_job(self, request: dict) -> dict:
        """Submit a video-generation request."""
        raise NotImplementedError

    @abstractmethod
    def get_job_status(self, job_id: str) -> dict:
        """Return the generation status."""
        raise NotImplementedError
