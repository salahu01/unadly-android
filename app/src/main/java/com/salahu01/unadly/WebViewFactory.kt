package com.salahu01.unadly

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.salahu01.unadly.browser.AdBlockingWebViewClient
import com.salahu01.unadly.browser.PopupBlockingChromeClient

/**
 * Factory for creating a fully-configured WebView with ad blocking and popup blocking.
 */
object WebViewFactory {

    @SuppressLint("SetJavaScriptEnabled")
    fun create(
        context: Context,
        onAdsBlocked: (Int) -> Unit,
        onUrlChanged: (String?) -> Unit,
        onTitleChanged: (String) -> Unit,
        onPageStarted: (String?) -> Unit,
        onPageFinished: (String?) -> Unit,
        onProgress: (Int) -> Unit
    ): WebView {
        val webView = WebView(context)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = false
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            loadWithOverviewMode = true
            useWideViewPort = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                safeBrowsingEnabled = true
            }
        }

        val client = AdBlockingWebViewClient(
            onAdsBlocked = onAdsBlocked,
            onUrlChanged = onUrlChanged,
            onTitleChanged = onTitleChanged,
            onPageStarted = onPageStarted,
            onPageFinished = onPageFinished
        )

        val chromeClient = PopupBlockingChromeClient(
            onProgress = onProgress,
            onTitle = onTitleChanged,
            onFaviconReceived = {},
            onShowHome = {}
        )

        webView.webViewClient = client
        webView.webChromeClient = chromeClient

        return webView
    }
}
