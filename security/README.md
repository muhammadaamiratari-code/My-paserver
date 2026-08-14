MY AI HUB — ADVANCED SECURITY LAYER

یہ فولڈر ہماری موجودہ Security کے اوپر ایک اضافی
Defensive Security Layer ہے۔

اس میں شامل ہے:

1. Central Security Configuration
2. Request Size Limits
3. Rate Limiting
4. Security Audit Logging
5. HTTP Security Headers
6. Origin / CSRF-style Protection
7. Server-side Authorization
8. Emergency Lockdown
9. Request IDs
10. Safe User-facing Error Responses


اہم اصول:

1. Gemini اور OpenAI API keys اس فولڈر یا Frontend میں
   محفوظ نہ کریں۔

2. Passwords، OTPs اور Master Tokens کو Source Code میں
   Hard-code نہ کریں۔

3. Production میں HTTPS استعمال کریں۔

4. TRUSTED_ORIGINS کو اپنی اصل HTTPS domain کے مطابق
   configure کریں۔

5. Multi-worker Production deployment میں Rate Limiting
   اور Emergency Lock state کو Redis یا Database میں منتقل
   کرنا بہتر ہے۔

6. Owner diagnostics صرف محفوظ Server-side logs میں رہیں۔

7. User کو Traceback، API Key، Password یا حساس technical
   information واپس نہ بھیجی جائے۔

8. حقیقی Authentication کے لیے Server-verified Sessions
   یا secure Tokens استعمال کیے جائیں۔

9. Client-side JavaScript کو Security Authority نہ سمجھا جائے۔
   اہم فیصلے Server پر دوبارہ verify کیے جائیں۔


Flask میں بنیادی استعمال:

from security.security_middleware import SecurityMiddleware

SecurityMiddleware(app)


یہ Security Layer موجودہ routes کو خود بخود replace نہیں کرتی۔
یہ ان کے اوپر ایک حفاظتی Middleware فراہم کرتی ہے۔
