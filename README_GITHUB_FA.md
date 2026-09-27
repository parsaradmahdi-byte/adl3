# راهنمای آپلود نسخه گرافیکی در GitHub

این نسخه مخصوص GitHub Actions است.

## اگر repository فعلی `adl` است
محتویات ZIP را در ریشه `adl` قرار بده، سپس:

```bash
cd /storage/emulated/0/Download/adl
git add .
git commit -m "Build Rah Edalat graphic MVP"
git push -u origin main
```

اگر GitHub نام کاربری/احراز هویت خواست، از روش احراز هویتی که قبلاً برای repository خودت استفاده کرده‌ای استفاده کن.

## بعد از push
GitHub → repository `adl` → Actions → `Build Rah Edalat APK`

پس از سبز شدن workflow:
Actions → اجرای موفق → Artifacts → `RahEdalat-Graphic-Debug-APK`

## نکته
این پروژه Gradle wrapper ندارد چون GitHub Actions خودش Gradle 8.7 را روی runner تنظیم می‌کند؛ بنابراین لازم نیست روی گوشی Gradle یا Android Studio نصب شود.
