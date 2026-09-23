# Material Calculator

تطبيق حاسبة Native لـ Android مبني بـ Kotlin وMaterial 3. التطبيق ليس HTML ولا WebView.

## المزايا

- واجهة Material 3 مع ألوان Dynamic Color على Android 12+.
- وضع فاتح وداكن حسب إعداد النظام.
- العمليات: الجمع والطرح والضرب والقسمة.
- الكسور العشرية والنسبة المئوية وتغيير الإشارة.
- حذف آخر رقم ومسح كامل.
- محرك حساب داخلي بـ BigDecimal لتقليل أخطاء الفاصلة العائمة.
- اختبارات وحدة للمحرك.
- GitHub Actions يبني APK Debug تلقائياً عند كل Push أو Pull Request.

## البناء

المشروع يستخدم Android Gradle Plugin 8.13.2 وGradle 8.13 وJDK 17.

لإنشاء APK Debug:

```bash
gradle --no-daemon assembleDebug
```

الناتج:

```
app/build/outputs/apk/debug/app-debug.apk
```

## الترخيص

MIT
