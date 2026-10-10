# Keep WebView classes (they're accessed by name via reflection in some OEM ROMs)
-keep class android.webkit.** { *; }

# Keep Compose runtime
-dontwarn androidx.compose.**

# Keep our model classes (serialized via SharedPreferences)
-keep class com.salahu01.unadly.** { *; }
