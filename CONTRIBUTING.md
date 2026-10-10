# Contributing

Thanks for helping make Unadly better. Bug reports, ideas and pull requests are all welcome.

## Build and run

Requirements: Android SDK 35 (platform `android-35`), JDK 17+, Kotlin 2.x.

```bash
export ANDROID_HOME=~/Library/Android/sdk   # or your SDK location
./gradlew assembleDebug                      # build the debug APK
# APK: app/build/outputs/apk/debug/app-debug.apk
adb install app/build/outputs/apk/debug/app-debug.apk
```

Or open the project in Android Studio and press Run.

## Project layout

| Path | What's there |
|---|---|
| `app/src/main/java/com/salahu01/unadly/` | App logic: ad blocker, browser view model, URL utils, WebView factory. |
| `app/src/main/java/com/salahu01/unadly/browser/` | Custom `WebViewClient` (ad blocking) and `WebChromeClient` (popup blocking). |
| `app/src/main/java/com/salahu01/unadly/data/` | Pin store (SharedPreferences). |
| `app/src/main/java/com/salahu01/unadly/ui/` | Jetpack Compose UI: browser screen, home screen, theme. |
| `app/src/main/assets/adhosts.txt` | The ad-server list (42,000+ hosts). |
| `app/src/main/res/` | App icon, strings, themes, colours. |

## Guidelines

- No analytics, telemetry or network requests other than the pages the user opens and the
  icons of their pinned sites.
- Ad lists must have a licence that allows redistribution. Credit them in `CREDITS.md` and `NOTICE`.
- Build with `./gradlew assembleDebug` before opening a pull request.
- Keep the app warning-free. Run `./gradlew lint` before submitting.

## Updating the ad list

The ad-server list comes from [anudeepND/blacklist](https://github.com/anudeepND/blacklist) (MIT).
Download the latest `domains.txt` and copy the ad hosts into `app/src/main/assets/adhosts.txt`,
one hostname per line (no `0.0.0.0` prefix — just the bare host).

## Releasing

1. Bump `versionCode` and `versionName` in `app/build.gradle.kts`, and add a `CHANGELOG.md` entry.
2. `./gradlew assembleRelease` (set a signing config for a signed build, or use `assembleDebug` for testing).
3. Tag the release (`git tag v1.0.1`) and let the Release workflow publish the APK.

By contributing you agree that your contributions are licensed under the Apache License 2.0.
