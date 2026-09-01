import json
import os
import urllib.parse
import urllib.request
import webbrowser
from datetime import datetime

PROJECT_ROOT = os.path.dirname(os.path.abspath(__file__))
BROWSER_RECORD = os.path.join(PROJECT_ROOT, "browser_test_record.json")


def _safe_url(url):
    if not isinstance(url, str) or not url.strip():
        raise ValueError("URL ضروری ہے۔")
    parsed = urllib.parse.urlparse(url.strip())
    if parsed.scheme not in ("http", "https") or not parsed.netloc:
        raise ValueError("صرف http یا https URL کی اجازت ہے۔")
    if parsed.username or parsed.password:
        raise ValueError("URL میں username/password کی اجازت نہیں۔")
    return url.strip()


def _load_record():
    if not os.path.exists(BROWSER_RECORD):
        return {"tests": []}
    try:
        with open(BROWSER_RECORD, "r", encoding="utf-8") as f:
            data = json.load(f)
        if not isinstance(data, dict):
            raise ValueError()
        if not isinstance(data.get("tests"), list):
            data["tests"] = []
        return data
    except Exception:
        return {"tests": []}


def _save_record(data):
    try:
        temp = BROWSER_RECORD + ".tmp"
        with open(temp, "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        os.replace(temp, BROWSER_RECORD)
        return True
    except Exception:
        return False


def _log_browser_test(test_type, url, success, output):
    record = _load_record()
    record["tests"].append({
        "timestamp": datetime.now().isoformat(),
        "test_type": test_type,
        "url": str(url),
        "success": bool(success),
        "output": str(output)[:4000],
    })
    record["tests"] = record["tests"][-200:]
    _save_record(record)


def open_browser(url):
    try:
        url = _safe_url(url)
        opened = webbrowser.open(url)
        message = "URL browser میں کھولنے کی درخواست کامیاب ہوئی۔" if opened else "Browser launch environment میں دستیاب نہیں۔ URL خود browser میں کھولا جا سکتا ہے۔"
        _log_browser_test("open_url", url, bool(opened), message)
        return bool(opened), message
    except Exception as e:
        _log_browser_test("open_url", str(url), False, str(e))
        return False, f"Browser launch میں مسئلہ: {e}"


def check_page_status(url, expected_status=200):
    try:
        url = _safe_url(url)
        request = urllib.request.Request(url, method="GET")
        with urllib.request.urlopen(request, timeout=10) as response:
            status = response.getcode()
            content = response.read().decode("utf-8", errors="ignore")
        success = status == expected_status
        output = f"Status: {status}\n\nContent preview:\n{content[:2000]}"
        _log_browser_test("page_status", url, success, output)
        return success, output
    except Exception as e:
        _log_browser_test("page_status", str(url), False, str(e))
        return False, f"Page test میں مسئلہ: {e}"


def test_form(url, form_data, method="POST"):
    try:
        url = _safe_url(url)
        method = method.upper()
        if not isinstance(form_data, dict):
            return False, "form_data dictionary ہونا چاہیے۔"
        if method == "POST":
            data = urllib.parse.urlencode(form_data).encode("utf-8")
            request = urllib.request.Request(url, data=data, method="POST", headers={"Content-Type": "application/x-www-form-urlencoded"})
        elif method == "GET":
            separator = "&" if "?" in url else "?"
            target = url + separator + urllib.parse.urlencode(form_data)
            request = urllib.request.Request(target, method="GET")
        else:
            return False, "صرف GET یا POST supported ہے۔"
        with urllib.request.urlopen(request, timeout=15) as response:
            status = response.getcode()
            content = response.read().decode("utf-8", errors="ignore")
        success = 200 <= status < 400
        output = f"Status: {status}\n\nResponse preview:\n{content[:3000]}"
        _log_browser_test("form_test", url, success, output)
        return success, output
    except Exception as e:
        _log_browser_test("form_test", str(url), False, str(e))
        return False, f"Form test میں مسئلہ: {e}"
