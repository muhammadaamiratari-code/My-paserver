
from __future__ import annotations

import json
import os
import re
import time
import threading
from dataclasses import dataclass, field
from collections import deque
from typing import Any, Callable, Dict, Iterable, Iterator, List, Optional

@dataclass
class ModelConfig:
    name: str
    provider: str
    capabilities: set[str] = field(default_factory=lambda: {"chat"})
    priority: int = 50
    cost_in: float = 0.0
    cost_out: float = 0.0
    enabled: bool = True

@dataclass
class RouterConfig:
    primary_model: str = field(default_factory=lambda: os.getenv(
        "AI_PRIMARY_MODEL", "PRIMARY_MODEL_NAME"))
    secondary_model: str = field(default_factory=lambda: os.getenv(
        "AI_SECONDARY_MODEL", "SECONDARY_MODEL_NAME"))
    fallback_model: str = field(default_factory=lambda: os.getenv(
        "AI_FALLBACK_MODEL", "FALLBACK_MODEL_NAME"))

    primary_provider: str = field(default_factory=lambda: os.getenv(
        "AI_PRIMARY_PROVIDER", "openrouter"))
    secondary_provider: str = field(default_factory=lambda: os.getenv(
        "AI_SECONDARY_PROVIDER", "openrouter"))
    fallback_provider: str = field(default_factory=lambda: os.getenv(
        "AI_FALLBACK_PROVIDER", "gemini"))

    max_attempts: int = 3
    base_timeout: float = 30.0
    max_timeout: float = 180.0
    retry_delay: float = 0.8
    max_retry_delay: float = 8.0
    daily_budget: float = float(os.getenv("AI_DAILY_BUDGET", "0"))
    cache_ttl: float = float(os.getenv("AI_CACHE_TTL", "900"))
    max_prompt_chars: int = 120000
    max_output_chars: int = 300000

@dataclass
class TaskContext:
    task_id: str
    task_type: str = "chat"
    preferred_model: Optional[str] = None
    active_model: Optional[str] = None
    provider: Optional[str] = None
    history: deque = field(default_factory=lambda: deque(maxlen=20))
    metadata: Dict[str, Any] = field(default_factory=dict)

class TaskContextManager:

    def __init__(self) -> None:
        self._tasks: Dict[str, TaskContext] = {}
        self._lock = threading.RLock()

    def get(self, task_id: str, task_type: str = "chat") -> TaskContext:
        with self._lock:
            if task_id not in self._tasks:
                self._tasks[task_id] = TaskContext(task_id, task_type)
            return self._tasks[task_id]

    def add(self, task_id: str, role: str, content: str) -> None:
        self.get(task_id).history.append({"role": role, "content": content})

    def set_affinity(self, task_id: str, model: str, provider: str) -> None:
        ctx = self.get(task_id)
        ctx.active_model, ctx.provider = model, provider

    def clear(self, task_id: str) -> None:
        with self._lock:
            self._tasks.pop(task_id, None)

class SmartTimeout:

    BASE = {
        "chat": 30,
        "coding": 90,
        "testing": 120,
        "research": 150,
        "vision": 90,
    }

    def __init__(self, maximum: float = 180.0) -> None:
        self.maximum = maximum

    def get(self, task_type: str, output_chars: int = 0) -> float:
        value = float(self.BASE.get(task_type, 60))
        if output_chars > 20000:
            value += 30
        if output_chars > 80000:
            value += 30
        return min(value, self.maximum)

class NetworkProtection:

    TRANSIENT = (
        TimeoutError, ConnectionError, OSError
    )

    def __init__(self, base_delay: float = 0.8, max_delay: float = 8.0) -> None:
        self.base_delay, self.max_delay = base_delay, max_delay

    def should_retry(self, exc: Exception) -> bool:
        text = str(exc).lower()
        markers = ("timeout", "timed out", "connection", "reset",
                   "temporarily", "unavailable", "503", "502", "504")
        return isinstance(exc, self.TRANSIENT) or any(x in text for x in markers)

    def delay(self, attempt: int) -> float:
        return min(self.base_delay * (2 ** max(0, attempt - 1)), self.max_delay)

class CircuitBreaker:

    def __init__(self, threshold: int = 3, cooldown: float = 60.0) -> None:
        self.threshold, self.cooldown = threshold, cooldown
        self._failures: Dict[str, int] = {}
        self._opened: Dict[str, float] = {}
        self._lock = threading.RLock()

    def allowed(self, key: str) -> bool:
        with self._lock:
            opened = self._opened.get(key)
            if opened is None:
                return True
            if time.monotonic() - opened >= self.cooldown:
                self._opened.pop(key, None)
                self._failures[key] = 0
                return True
            return False

    def success(self, key: str) -> None:
        with self._lock:
            self._failures.pop(key, None)
            self._opened.pop(key, None)

    def failure(self, key: str) -> None:
        with self._lock:
            count = self._failures.get(key, 0) + 1
            self._failures[key] = count
            if count >= self.threshold:
                self._opened[key] = time.monotonic()

class CostTracker:

    def __init__(self) -> None:
        self.records: List[Dict[str, Any]] = []
        self._lock = threading.RLock()

    def record(self, model: str, input_tokens: int = 0,
               output_tokens: int = 0, cost_in: float = 0.0,
               cost_out: float = 0.0) -> Dict[str, Any]:
        cost = (input_tokens / 1_000_000) * cost_in
        cost += (output_tokens / 1_000_000) * cost_out
        row = {
            "model": model,
            "input_tokens": int(input_tokens),
            "output_tokens": int(output_tokens),
            "estimated_cost": round(cost, 10),
            "timestamp": time.time(),
        }
        with self._lock:
            self.records.append(row)
        return row

    def today_cost(self) -> float:
        start = time.time() - 86400
        with self._lock:
            return sum(x["estimated_cost"] for x in self.records
                       if x["timestamp"] >= start)

class HealthManager:

    def __init__(self) -> None:
        self.state: Dict[str, Dict[str, Any]] = {}

    def success(self, key: str, latency: float) -> None:
        self.state[key] = {"healthy": True, "latency": latency,
                           "last_check": time.time()}

    def failure(self, key: str, error: str) -> None:
        self.state[key] = {"healthy": False, "error": error,
                           "last_check": time.time()}

    def is_healthy(self, key: str) -> bool:
        return self.state.get(key, {}).get("healthy", True)

class QualityGate:

    def __init__(self, max_chars: int = 300000) -> None:
        self.max_chars = max_chars

    def check(self, text: Any) -> tuple[bool, str]:
        if not isinstance(text, str):
            return False, "response is not text"
        if not text.strip():
            return False, "empty response"
        if len(text) > self.max_chars:
            return False, "response exceeds configured limit"
        return True, ""

class CapabilityMatcher:

    REQUIRED = {
        "chat": {"chat"},
        "coding": {"chat", "coding"},
        "testing": {"chat", "coding"},
        "research": {"chat", "reasoning"},
        "vision": {"vision"},
        "json": {"chat", "structured"},
    }

    def matches(self, model: ModelConfig, task_type: str) -> bool:
        needed = self.REQUIRED.get(task_type, {"chat"})
        return model.enabled and bool(needed.intersection(model.capabilities))

class BudgetGuard:

    def __init__(self, tracker: CostTracker, daily_budget: float) -> None:
        self.tracker, self.daily_budget = tracker, daily_budget

    def allowed(self, estimated_cost: float = 0.0) -> bool:
        if self.daily_budget <= 0:
            return True
        return self.tracker.today_cost() + estimated_cost <= self.daily_budget

class ContextCompressor:

    def compress(self, messages: Iterable[Dict[str, Any]],
                 max_chars: int = 80000) -> List[Dict[str, Any]]:
        items = list(messages)
        total = sum(len(str(x.get("content", ""))) for x in items)
        if total <= max_chars:
            return items
        result: List[Dict[str, Any]] = []
        used = 0
        for item in reversed(items):
            text = str(item.get("content", ""))
            if used + len(text) > max_chars:
                remaining = max_chars - used
                if remaining > 0:
                    result.append({**item, "content": text[-remaining:]})
                break
            result.append(item)
            used += len(text)
        return list(reversed(result))

class ChangeRollbackGuard:

    def __init__(self) -> None:
        self.history: List[Dict[str, Any]] = []

    def record(self, action: str, data: Optional[Dict[str, Any]] = None) -> None:
        self.history.append({"action": action, "data": data or {},
                             "timestamp": time.time()})

    def last(self) -> Optional[Dict[str, Any]]:
        return self.history[-1] if self.history else None

class SmartCache:

    def __init__(self, ttl: float = 900.0) -> None:
        self.ttl = ttl
        self._data: Dict[str, tuple[float, Any]] = {}
        self._lock = threading.RLock()

    def get(self, key: str) -> Any:
        with self._lock:
            row = self._data.get(key)
            if not row:
                return None
            stamp, value = row
            if time.time() - stamp > self.ttl:
                self._data.pop(key, None)
                return None
            return value

    def set(self, key: str, value: Any) -> None:
        with self._lock:
            self._data[key] = (time.time(), value)

class PromptResponseCompressor:

    def prompt(self, text: str) -> str:
        text = text.replace("\r\n", "\n").replace("\r", "\n")
        lines = [line.rstrip() for line in text.split("\n")]
        cleaned: List[str] = []
        blank = False
        for line in lines:
            if not line.strip():
                if not blank:
                    cleaned.append("")
                blank = True
            else:
                cleaned.append(line)
                blank = False
        return "\n".join(cleaned).strip()

    def response(self, text: str) -> str:
        return text.rstrip()

class RateLimitGuard:

    def __init__(self, max_wait: float = 30.0) -> None:
        self.max_wait = max_wait

    def retry_after(self, error: Any) -> float:
        text = str(error)
        match = re.search(r"(?:retry[- ]after|retry in)\\s*[:=]?\\s*(\\d+(?:\\.\\d+)?)",
                          text, re.I)
        if match:
            return min(float(match.group(1)), self.max_wait)
        return 2.0

class OutputParserGuard:

    def parse_json(self, value: Any) -> Any:
        if isinstance(value, (dict, list)):
            return value
        text = str(value).strip()
        try:
            return json.loads(text)
        except json.JSONDecodeError:
            starts = [p for p in (text.find("{"), text.find("[")) if p >= 0]
            if not starts:
                raise
            start = min(starts)
            end = max(text.rfind("}"), text.rfind("]"))
            if end <= start:
                raise
            return json.loads(text[start:end + 1])

class StreamingAdapter:

    def chunks(self, response: Any) -> Iterator[str]:
        if response is None:
            return
        if isinstance(response, str):
            yield response
            return
        if isinstance(response, dict):
            text = response.get("text", "")
            if text:
                yield str(text)
            return
        if hasattr(response, "__iter__"):
            for item in response:
                if isinstance(item, str):
                    yield item
                elif isinstance(item, dict):
                    text = item.get("text") or item.get("delta") or ""
                    if text:
                        yield str(text)
                else:
                    text = getattr(item, "text", None) or getattr(item, "delta", None)
                    if text:
                        yield str(text)

class PromptSanitizer:

    PATTERNS = (
        r"ignore\\s+(all|any|previous|prior)\\s+instructions",
        r"reveal\\s+(the\\s+)?system\\s+prompt",
        r"show\\s+(me\\s+)?(the\\s+)?hidden\\s+instructions",
        r"developer\\s+message\\s*:",
    )

    def inspect(self, text: str) -> Dict[str, Any]:
        hits = [p for p in self.PATTERNS if re.search(p, text, re.I)]
        return {"suspicious": bool(hits), "matches": len(hits)}

    def sanitize(self, text: str) -> str:
        return text

class ModelLimitsSync:

    def apply(self, models: Iterable[ModelConfig],
              metadata: Optional[Dict[str, Dict[str, Any]]] = None) -> List[ModelConfig]:
        metadata = metadata or {}
        result = []
        for model in models:
            data = metadata.get(model.name, {})
            if "cost_in" in data:
                model.cost_in = float(data["cost_in"])
            if "cost_out" in data:
                model.cost_out = float(data["cost_out"])
            result.append(model)
        return result

class APIRegistry:

    def __init__(self, config: RouterConfig) -> None:
        self.models = [
            ModelConfig(config.primary_model, config.primary_provider,
                        {"chat", "coding", "reasoning", "structured"}, 100),
            ModelConfig(config.secondary_model, config.secondary_provider,
                        {"chat", "coding", "reasoning", "structured"}, 90),
            ModelConfig(config.fallback_model, config.fallback_provider,
                        {"chat", "coding", "reasoning", "vision", "structured"}, 80),
        ]

    def all(self) -> List[ModelConfig]:
        return [m for m in self.models if m.enabled]

class AIRouter:

    def __init__(self, config: Optional[RouterConfig] = None,
                 request_callable: Optional[Callable[..., Any]] = None,
                 memory_get: Optional[Callable[..., Any]] = None,
                 memory_set: Optional[Callable[..., Any]] = None) -> None:
        self.config = config or RouterConfig()
        self.registry = APIRegistry(self.config)
        self.contexts = TaskContextManager()
        self.timeout = SmartTimeout(self.config.max_timeout)
        self.network = NetworkProtection(self.config.retry_delay,
                                         self.config.max_retry_delay)
        self.breaker = CircuitBreaker()
        self.cost = CostTracker()
        self.health = HealthManager()
        self.quality = QualityGate(self.config.max_output_chars)
        self.capability = CapabilityMatcher()
        self.budget = BudgetGuard(self.cost, self.config.daily_budget)
        self.context_compressor = ContextCompressor()
        self.change_guard = ChangeRollbackGuard()
        self.cache = SmartCache(self.config.cache_ttl)
        self.compressor = PromptResponseCompressor()
        self.rate_limit = RateLimitGuard()
        self.parser = OutputParserGuard()
        self.streaming = StreamingAdapter()
        self.sanitizer = PromptSanitizer()
        self.limits_sync = ModelLimitsSync()
        self.request_callable = request_callable
        self.memory_get = memory_get
        self.memory_set = memory_set

    def select_model(self, task_type: str = "chat",
                     preferred_model: Optional[str] = None,
                     task_id: Optional[str] = None) -> ModelConfig:
        models = self.registry.all()
        if preferred_model:
            for model in models:
                if model.name == preferred_model and self.capability.matches(model, task_type):
                    return model

        if task_id:
            ctx = self.contexts.get(task_id, task_type)
            if ctx.active_model:
                for model in models:
                    if model.name == ctx.active_model and self.capability.matches(model, task_type):
                        return model

        candidates = [m for m in models if self.capability.matches(m, task_type)]
        if not candidates:
            raise RuntimeError(f"No enabled model supports task type: {task_type}")
        return sorted(candidates, key=lambda m: m.priority, reverse=True)[0]

    def _fallbacks(self, selected: ModelConfig, task_type: str) -> List[ModelConfig]:
        candidates = [m for m in self.registry.all()
                      if m.name != selected.name and self.capability.matches(m, task_type)]
        return sorted(candidates, key=lambda m: m.priority, reverse=True)

    def _cache_key(self, prompt: str, task_type: str, model: str) -> str:
        return f"{task_type}|{model}|{prompt.strip()}"

    def _normalize_response(self, raw: Any) -> Dict[str, Any]:
        if isinstance(raw, dict):
            text = raw.get("text")
            if text is None:
                text = raw.get("content", "")
            usage = raw.get("usage", {}) or {}
            return {"text": str(text or ""), "usage": usage}
        return {"text": str(raw or ""), "usage": {}}

    def _call_provider(self, model: ModelConfig, prompt: str,
                       task_type: str, timeout: float, **kwargs: Any) -> Any:
        if self.request_callable is None:
            raise RuntimeError(
                "No request_callable configured. Connect server.py/provider "
                "adapters before making live API calls."
            )
        return self.request_callable(
            provider=model.provider,
            model=model.name,
            prompt=prompt,
            task_type=task_type,
            timeout=timeout,
            **kwargs,
        )

    def route(self, prompt: str, task_type: str = "chat",
              task_id: Optional[str] = None,
              preferred_model: Optional[str] = None,
              force_online: bool = False,
              stream: bool = False,
              require_json: bool = False,
              **kwargs: Any) -> Dict[str, Any]:
        if not isinstance(prompt, str) or not prompt.strip():
            return {"ok": False, "text": "", "model": None, "provider": None,
                    "source": "validation", "attempts": 0,
                    "usage": {}, "error": "Empty prompt"}

        task_id = task_id or f"task-{id(prompt)}"
        ctx = self.contexts.get(task_id, task_type)

        inspection = self.sanitizer.inspect(prompt)
        prompt = self.sanitizer.sanitize(prompt)

        prompt = self.compressor.prompt(prompt)
        if len(prompt) > self.config.max_prompt_chars:
            prompt = prompt[-self.config.max_prompt_chars:]

        selected = self.select_model(task_type, preferred_model, task_id)
        cache_key = self._cache_key(prompt, task_type, selected.name)
        if not force_online:
            cached = self.cache.get(cache_key)
            if cached is not None:
                return {**cached, "source": "cache"}

            if self.memory_get:
                try:
                    remembered = self.memory_get(prompt=prompt, task_type=task_type)
                    if remembered:
                        return {
                            "ok": True, "text": str(remembered),
                            "model": None, "provider": None,
                            "source": "local_memory", "attempts": 0,
                            "usage": {}, "error": None,
                        }
                except Exception:
                    pass

        if not self.budget.allowed():
            return {"ok": False, "text": "", "model": None, "provider": None,
                    "source": "budget_guard", "attempts": 0, "usage": {},
                    "error": "Daily AI budget limit reached"}

        models = [selected] + self._fallbacks(selected, task_type)
        total_attempts = 0
        last_error = None

        for model in models:
            key = f"{model.provider}:{model.name}"

            if not self.breaker.allowed(key):
                continue

            timeout = self.timeout.get(task_type, len(prompt))
            started = time.monotonic()

            for attempt in range(1, self.config.max_attempts + 1):
                total_attempts += 1
                try:
                    raw = self._call_provider(model, prompt, task_type, timeout,
                                              stream=stream, **kwargs)
                    normalized = self._normalize_response(raw)

                    if stream:
                        normalized["text"] = "".join(
                            self.streaming.chunks(raw)
                        ) or normalized["text"]

                    normalized["text"] = self.compressor.response(normalized["text"])

                    good, reason = self.quality.check(normalized["text"])
                    if not good:
                        raise ValueError(f"Quality gate: {reason}")

                    if require_json:
                        parsed = self.parser.parse_json(normalized["text"])
                        normalized["text"] = json.dumps(
                            parsed, ensure_ascii=False, separators=(",", ":")
                        )

                    latency = time.monotonic() - started
                    self.health.success(key, latency)
                    self.breaker.success(key)

                    usage = normalized.get("usage", {}) or {}
                    in_tokens = int(usage.get("input_tokens",
                                      usage.get("prompt_tokens", 0)) or 0)
                    out_tokens = int(usage.get("output_tokens",
                                       usage.get("completion_tokens", 0)) or 0)
                    cost_row = self.cost.record(
                        model.name, in_tokens, out_tokens,
                        model.cost_in, model.cost_out
                    )

                    result = {
                        "ok": True,
                        "text": normalized["text"],
                        "model": model.name,
                        "provider": model.provider,
                        "source": "api",
                        "attempts": total_attempts,
                        "usage": {**usage, **cost_row},
                        "error": None,
                        "sanitation": inspection,
                    }

                    self.contexts.set_affinity(task_id, model.name, model.provider)
                    self.contexts.add(task_id, "user", prompt)
                    self.contexts.add(task_id, "assistant", normalized["text"])
                    self.change_guard.record("successful_request",
                                             {"task_id": task_id,
                                              "model": model.name})

                    self.cache.set(cache_key, result)
                    if self.memory_set:
                        try:
                            self.memory_set(prompt=prompt,
                                            response=normalized["text"],
                                            task_type=task_type,
                                            model=model.name)
                        except Exception:
                            pass
                    return result

                except Exception as exc:
                    last_error = str(exc)
                    self.health.failure(key, last_error)
                    self.breaker.failure(key)

                    if "429" in last_error.lower() or "rate limit" in last_error.lower():
                        delay = self.rate_limit.retry_after(exc)
                        if attempt < self.config.max_attempts:
                            time.sleep(delay)
                            continue

                    if self.network.should_retry(exc) and attempt < self.config.max_attempts:
                        time.sleep(self.network.delay(attempt))
                        continue

                    break

        return {
            "ok": False,
            "text": "",
            "model": None,
            "provider": None,
            "source": "fallback_exhausted",
            "attempts": total_attempts,
            "usage": {},
            "error": last_error or "All configured APIs failed",
        }

__all__ = [
    "AIRouter",
    "RouterConfig",
    "ModelConfig",
    "TaskContext",
    "TaskContextManager",
]
