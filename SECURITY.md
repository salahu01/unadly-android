# Security policy

Unadly for Android is a web browser built on Android's WebView. It has no accounts, no server of its own
and no analytics. Pages load straight from each website; Unadly never sees or relays your traffic.

What it stores on your phone:

- Pinned sites: the app's SharedPreferences (`com.salahu01.unadly`)
- Cookies, cache and site data: WebView's default website data store for the app
- Downloads: saved to your Downloads folder

Web content runs in Android's WebView sandbox. Popups a page opens without a click are blocked.
Ad requests to known ad servers are intercepted and returned empty before they leave the phone.

## Reporting a vulnerability

Please **do not open a public issue**. Use GitHub's private reporting: open the repository's
**Security** tab and choose **Report a vulnerability**. You can expect an acknowledgement within a
few days.

Supported version: the latest release.
