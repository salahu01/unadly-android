<img src="app/src/main/res/drawable/AppIcon.png" width="128" align="right" alt="Unadly icon">

# Unadly (Android)

**An Android web browser with ads blocked before they load.**

Open any site and the ads simply aren't there. Unadly blocks about 42,000 ad servers inside
the browser engine, so ad requests never leave your phone, and pages load faster with less
data. Popups and popunders that sites open on their own are dropped too.

It runs on Android WebView, so sites, logins and video work the way they do in your browser.
No account, no server in between, no tracking. Free and open source.

## Features

**🚫 Ads blocked in the engine.** A custom `WebViewClient` intercepts every request and blocks
any whose host (or parent domain) is in the ad-server list. The list comes from
[anudeepND/blacklist](https://github.com/anudeepND/blacklist) (MIT) and is bundled in
`app/src/main/assets/adhosts.txt`.

**🪟 No popups.** A window a page tries to open by itself is dropped. Links you click still open.

**📌 Pinned sites.** Pin a site with the pin button and it appears on the home page with its icon.

**🔍 Search or address.** Type a site address or a search (DuckDuckGo) on the home page or in the
toolbar.

**▶️ Video and downloads.** Video and full screen work as in any browser.

**📊 Ad counter.** See how many ads were blocked on the current page.

## Install

1. Download the APK from the [latest release](https://github.com/salahu01/unadly-android/releases/latest).
2. Enable "Install unknown apps" for your browser, then install the APK.
3. Requires Android 8.0 (API 26) or later.

## Privacy

- Pages load straight from each website. There is no Unadly server.
- No analytics, telemetry or crash reporting.
- Searches go to DuckDuckGo. The app fetches the icons of sites you pin.
- Pins are stored in the app's SharedPreferences on your phone.

## Build from source

```bash
git clone https://github.com/salahu01/unadly-android.git
cd unadly-android
export ANDROID_HOME=~/Library/Android/sdk
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

Requires Android SDK 35, JDK 17+, and Kotlin 2.1+. Uses Gradle 8.10.2 via the wrapper.

## How ad blocking works

`app/src/main/assets/adhosts.txt` lists ad-server hostnames. At launch, Unadly loads them into a
`HashSet` in a background thread for O(1) lookups. Every request the WebView makes passes through
`shouldInterceptRequest`, which checks whether the request is third-party (its host differs from
the page host) and whether the request host or any parent domain is in the set. If so, the request
is blocked with an empty response.

The list comes from [anudeepND/blacklist](https://github.com/anudeepND/blacklist) (MIT).

## License

[Apache License 2.0](LICENSE). The bundled ad list is MIT licensed.
