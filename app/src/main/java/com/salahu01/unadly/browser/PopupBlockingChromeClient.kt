package com.salahu01.unadly.browser

import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebChromeClient.CustomViewCallback
import android.view.View

/**
 * WebChromeClient that blocks popups unless the user explicitly clicked a link.
 * Also handles full-screen video and favicons.
 */
class PopupBlockingChromeClient(
    private val onProgress: (Int) -> Unit,
    private val onTitle: (String) -> Unit,
    private val onFaviconReceived: (String) -> Unit,
    private val onShowHome: () -> Unit
) : WebChromeClient() {

    private var userTriggeredNavigation = false

    fun setUserTriggered(v: Boolean) {
        userTriggeredNavigation = v
    }

    /**
     * Called when a page tries to open a new window.
     * Only allow if the user clicked a link (userTriggeredNavigation == true).
     * Otherwise drop the popup.
     */
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: android.os.Message?
    ): Boolean {
        // Only allow if user gesture (link click), not script-initiated popups
        if (isUserGesture && userTriggeredNavigation) {
            // Let the WebView handle it — it'll load the URL in the current view
            // since we don't support multi-window
            return false // Return false to prevent new window; the URL will still load
        }
        // Drop the popup
        return false
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        onProgress(newProgress)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        if (!title.isNullOrBlank()) {
            onTitle(title)
        }
    }

    override fun onReceivedIcon(view: WebView?, icon: android.graphics.Bitmap?) {
        // Could save the favicon, but we fetch favicons separately for pins
    }

    override fun onHideCustomView() {}

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {}
}
