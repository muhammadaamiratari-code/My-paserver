from __future__ import annotations

import re
import time
from concurrent.futures import ThreadPoolExecutor
from dataclasses import dataclass, field
from typing import Any, Callable, Dict, List, Optional


@dataclass
class ConversationState:
    status: str = "IDLE"
    intent: str = "unknown"
    topic: Optional[str] = None
    entities: Dict[str, str] = field(default_factory=dict)
    last_user_message: str = ""
    last_assistant_message: str = ""
    last_updated: float = field(default_factory=time.time)


@dataclass
class EngineResult:
    reply: Optional[str]
    intent: str
    confidence: float
    language: str
    script: str
    state: str
    route: str
    fast_track: bool
    needs_clarification: bool
    diagnostics: Dict[str, Any] = field(default_factory=dict)


class NaturalConversationEngine:
    CONVERSATION_TTL_SECONDS = 20 * 60
    CONFIDENCE_THRESHOLD = 0.62
    FAST_TRACK_MAX_CHARS = 120

    URDU_RE = re.compile(r"[\u0600-\u06FF]")
    EN_RE = re.compile(r"[A-Za-z]")

    GREETINGS = {
        "hi", "hello", "hey", "salam", "assalamualaikum",
        "assalam o alaikum", "aoa", "اسلام علیکم", "السلام علیکم",
        "ہیلو", "سلام"
    }
    THANKS = {
        "thanks", "thank you", "thx", "shukriya", "شکریہ",
        "بہت شکریہ", "مہربانی"
    }
    CASUAL = {
        "ok", "okay", "acha", "theek", "ٹھیک", "اچھا", "جی",
        "yes", "no", "haan", "han", "ہاں", "نہیں", "nope"
    }

    ROUTE_HINTS = {
        "coding": ("code", "coding", "python", "javascript", "bug", "error",
                   "function", "class", "script", "کوڈ", "بگ", "ایرر"),
        "research": ("research", "researching", "compare", "comparison",
                     "تحقیق", "موازنہ"),
        "system": ("server", "termux", "git", "github", "api", "database",
                   "سرور", "ٹرمکس", "ڈیٹا بیس"),
        "creative": ("write", "story", "poem", "caption", "کہانی", "شاعری"),
        "general_knowledge": ("پاکستان", "انڈیا", "بھارت", "دنیا", "ملک", "دارالحکومت", "تاریخ", "جغرافیہ", "capital", "country", "history", "geography"),
    }

    def __init__(
        self,
        generate_callback: Optional[Callable[..., str]] = None,
        memory_search_callback: Optional[Callable[..., Any]] = None,
        truth_callback: Optional[Callable[..., bool]] = None,
    ):
        self.generate_callback = generate_callback
        self.memory_search_callback = memory_search_callback
        self.truth_callback = truth_callback
        self.sessions: Dict[str, ConversationState] = {}
        self.executor = ThreadPoolExecutor(max_workers=4)

    def process(self, message: str, session_id: str = "default") -> EngineResult:
        message = (message or "").strip()
        now = time.time()
        state = self.sessions.setdefault(session_id, ConversationState())

        if now - state.last_updated > self.CONVERSATION_TTL_SECONDS:
            self._expire_active_state(state)

        if not message:
            return self._result(
                None, "empty", 1.0, "unknown", "unknown", state,
                "conversation", False, True, {"reason": "empty_message"}
            )

        language, script = self._detect_language(message)
        intent, confidence = self._detect_intent(message)
        is_followup = bool(state.topic) and any(ref in message.lower().split() for ref in ("یہ", "وہ", "اس", "اسے", "پچھلا", "that", "this", "it"))
        if is_followup and intent == "casual":
            intent, confidence = "question", 0.84

        if self._is_fast_track(message, intent):
            reply = self._fast_reply(message, intent)
            if reply:
                self._update_state(state, message, reply, intent)
                return self._result(
                    reply, intent, confidence, language, script, state,
                    "conversation", True, False,
                    {"gates": 2, "path": "fast_track"}
                )

        route = self._route_hint(message, intent)
        if is_followup and state.topic:
            route = self._route_hint(state.topic, state.intent)

        if route == "general_knowledge":
            return self._result(
                None, intent, confidence, language, script, state,
                "general_knowledge", False, False,
                {"path": "handoff", "target_engine": "general_knowledge"}
            )

        if confidence < self.CONFIDENCE_THRESHOLD and self._needs_clarification(
            message, intent
        ):
            reply = self._clarification_reply(language)
            self._update_state(state, message, reply, "clarification")
            state.status = "WAITING_FOR_CLARIFICATION"
            return self._result(
                reply, intent, confidence, language, script, state,
                "conversation", False, True,
                {"path": "clarification", "threshold": self.CONFIDENCE_THRESHOLD}
            )

        context = self._get_context(session_id, state, message)
        reply = self._generate(message, intent, context, language, route)

        if reply is None:
            reply = self._safe_fallback(language)
            state.status = "WAITING_FOR_GENERATION"
        else:
            if state.status != "WAITING_FOR_GENERATION":
                state.status = "ACTIVE"

        gate_results = self._run_parallel_gates(reply, message)

        if not all(gate_results.values()):
            reply = self._safe_fallback(language)

        self._update_state(state, message, reply, intent)

        return self._result(
            reply, intent, confidence, language, script, state,
            route, False, False,
            {
                "path": "normal",
                "gates": gate_results,
                "context_used": bool(context),
            }
        )

    def reset_session(self, session_id: str = "default") -> None:
        self.sessions.pop(session_id, None)

    def get_state(self, session_id: str = "default") -> ConversationState:
        state = self.sessions.setdefault(session_id, ConversationState())
        if time.time() - state.last_updated > self.CONVERSATION_TTL_SECONDS:
            self._expire_active_state(state)
        return state

    def _detect_language(self, text: str):
        urdu = len(self.URDU_RE.findall(text))
        english = len(self.EN_RE.findall(text))

        if urdu and english:
            return "mixed", "mixed"
        if urdu:
            return "urdu", "urdu_script"
        if english:
            roman_markers = (
                "hai", "hain", "ho", "ka", "ki", "ke", "mein",
                "mujhe", "ap", "aap", "kya", "acha", "theek"
            )
            low_clean = re.sub(r'[^\w\s]', '', text.lower())
            if any(x in low_clean.split() for x in roman_markers):
                return "roman_urdu", "latin"
            return "english", "latin"
        return "unknown", "unknown"

    def _detect_intent(self, text: str):
        low = re.sub(r"\s+", " ", text.lower()).strip()

        if low in self.GREETINGS:
            return "greeting", 0.99
        if low in self.THANKS:
            return "thanks", 0.99
        if low in self.CASUAL:
            return "casual", 0.95

        if "?" in text or any(
            low.startswith(x) for x in
            ("what ", "why ", "how ", "when ", "where ", "who ",
             "کیا ", "کیوں ", "کیسے ", "کب ", "کہاں ", "کون ")
        ):
            return "question", 0.88

        if any(
            low.startswith(x) for x in
            ("please ", "can you ", "could you ", "help me ",
             "براہ کرم", "مجھے ", "کر دیں", "بتائیں")
        ):
            return "request", 0.82

        if any(x in low for x in ("remember", "یاد رکھ", "یاد ہے", "save this")):
            return "memory", 0.86

        if any(x in low for x in ("fix", "repair", "solve", "درست", "حل")):
            return "problem_solving", 0.80

        if low in {"یہ", "وہ", "اس", "اسے", "پچھلا", "that", "this", "it"}:
            return "unknown", 0.40

        if len(low.split()) <= 5:
            return "casual", 0.70

        return "conversation", 0.66

    def _is_fast_track(self, text: str, intent: str) -> bool:
        return (
            len(text) <= self.FAST_TRACK_MAX_CHARS
            and intent in {"greeting", "thanks", "casual"}
        )

    def _fast_reply(self, text: str, intent: str) -> Optional[str]:
        low = text.lower().strip()

        if intent == "greeting":
            if "salam" in low or "السلام" in text or "اسلام" in text:
                return "وعلیکم السلام عامر سر، کیسے ہیں؟"
            return "السلام علیکم عامر سر، کیسے ہیں؟"

        if intent == "thanks":
            return "خوش آمدید عامر سر۔"

        if intent == "casual":
            if low in {"ok", "okay", "theek", "acha"} or text in {"ٹھیک", "اچھا", "جی"}:
                return "جی عامر سر۔"
            if low in {"yes", "haan", "han"} or text == "ہاں":
                return "جی، بالکل عامر سر۔"
            if low in {"no", "nope"} or text == "نہیں":
                return "ٹھیک ہے عامر سر۔"

        return None

    def _route_hint(self, message: str, intent: str) -> str:
        low = message.lower()
        scores = {}
        for route, words in self.ROUTE_HINTS.items():
            scores[route] = sum(1 for word in words if word in low or word in message)

        best = max(scores, key=scores.get) if scores else "conversation"
        return best if scores.get(best, 0) else "conversation"

    def _needs_clarification(self, message: str, intent: str) -> bool:
        if intent in {"greeting", "thanks", "casual"}:
            return False
        words = message.split()
        return len(words) <= 2 or message.lower() in {"it", "this", "that", "یہ", "وہ", "اس"}

    def _clarification_reply(self, language: str) -> str:
        if language in {"urdu", "roman_urdu", "mixed"}:
            return "جی عامر سر، ذرا بتا دیں آپ کس چیز کی بات کر رہے ہیں؟"
        return "Sure. Could you clarify what you mean?"

    def _get_context(self, session_id: str, state: ConversationState, message: str):
        context = {
            "active_intent": state.intent,
            "topic": state.topic,
            "entities": dict(state.entities),
            "last_user_message": state.last_user_message,
            "last_assistant_message": state.last_assistant_message,
        }

        if self.memory_search_callback:
            try:
                memory = self.memory_search_callback(
                    query=message, session_id=session_id, limit=5
                )
                context["memory"] = memory
            except Exception:
                context["memory"] = None

        return context

    def _generate(self, message, intent, context, language, route):
        if not self.generate_callback:
            return None

        try:
            return self.generate_callback(
                message=message,
                intent=intent,
                context=context,
                language=language,
                route=route,
            )
        except TypeError:
            try:
                return self.generate_callback(message, context)
            except Exception:
                return None
        except Exception:
            return None

    def _run_parallel_gates(self, reply: str, user_message: str):
        gates = {
            "quality": self._quality_gate,
            "privacy": self._privacy_gate,
            "safety": self._safety_gate,
            "truth": self._truth_gate,
        }

        results = {}
        futures = {
            name: self.executor.submit(fn, reply, user_message)
            for name, fn in gates.items()
        }
        for name, future in futures.items():
                try:
                    results[name] = bool(future.result())
                except Exception:
                    results[name] = False
        return results

    def _quality_gate(self, reply: str, user_message: str) -> bool:
        return bool(reply and reply.strip())

    def _privacy_gate(self, reply: str, user_message: str) -> bool:
        blocked = ("password", "private key", "secret key", "api key", "ذاتی معلومات", "نجی معلومات")
        text = (reply + " " + user_message).lower()
        return not any(x in text for x in blocked)

    def _safety_gate(self, reply: str, user_message: str) -> bool:
        blocked = ("ذاتی معلومات", "نجی معلومات", "private information", "personal information")
        text = user_message.lower()
        return not any(x in text for x in blocked)

    def _truth_gate(self, reply: str, user_message: str) -> bool:
        if self.truth_callback:
            try:
                return bool(self.truth_callback(reply=reply, user_message=user_message))
            except Exception:
                return False
        return True

    def _update_state(
        self,
        state: ConversationState,
        user_message: str,
        reply: str,
        intent: str,
    ):
        state.last_user_message = user_message
        state.last_assistant_message = reply or ""
        state.intent = intent
        if state.status != "WAITING_FOR_GENERATION":
            state.status = "ACTIVE"
        state.last_updated = time.time()

        words = user_message.split()
        if words and not any(ref in user_message.lower().split() for ref in ("یہ", "وہ", "اس", "اسے", "پچھلا", "that", "this", "it")):
            state.topic = " ".join(words[:8])

        for ref in ("یہ", "وہ", "اس", "اسے", "پچھلا", "that", "this", "it"):
            if ref in user_message.lower() or ref in user_message:
                state.entities["reference"] = ref

    def _expire_active_state(self, state: ConversationState):
        state.status = "IDLE"
        state.intent = "unknown"
        state.topic = None
        state.entities.clear()
        state.last_user_message = ""
        state.last_assistant_message = ""
        state.last_updated = time.time()

    def _safe_fallback(self, language: str) -> str:
        if language in {"urdu", "roman_urdu", "mixed"}:
            return "جی عامر سر، اس وقت میں اس کا درست جواب تیار نہیں کر سکا۔"
        return "I couldn't prepare a reliable answer for that right now."

    def _result(
        self,
        reply,
        intent,
        confidence,
        language,
        script,
        state,
        route,
        fast_track,
        needs_clarification,
        diagnostics,
    ):
        return EngineResult(
            reply=reply,
            intent=intent,
            confidence=round(confidence, 3),
            language=language,
            script=script,
            state=state.status,
            route=route,
            fast_track=fast_track,
            needs_clarification=needs_clarification,
            diagnostics=diagnostics,
        )


__all__ = ["NaturalConversationEngine", "ConversationState", "EngineResult"]
