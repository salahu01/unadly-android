package com.salahu01.unadly.browser

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.salahu01.unadly.AdBlocker
import com.salahu01.unadly.UrlUtils
import java.io.ByteArrayInputStream

/**
 * WebViewClient that intercepts all requests and blocks ad-server requests.
 * Tracks the count of blocked ads per page load.
 */
class AdBlockingWebViewClient(
    private val onAdsBlocked: (Int) -> Unit,
    private val onUrlChanged: (String?) -> Unit,
    private val onTitleChanged: (String) -> Unit,
    private val onPageStarted: (String?) -> Unit,
    private val onPageFinished: (String?) -> Unit
) : WebViewClient() {

    private var blockedCount = 0
    private var currentUrl: String? = null

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        if (view == null || request == null) return null

        val requestUrl = request.url
        val requestHost = requestUrl.host
        val pageHost = UrlUtils.host(currentUrl)

        if (AdBlocker.shouldBlock(requestHost, pageHost)) {
            blockedCount++
            onAdsBlocked(blockedCount)
            // Return an empty response to block the request
            return WebResourceResponse(
                "text/plain",
                "utf-8",
                ByteArrayInputStream(ByteArray(0))
            )
        }
        return null // Let WebView handle normally
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        currentUrl = url
        blockedCount = 0
        onAdsBlocked(0)
        onPageStarted(url)
        onUrlChanged(url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        onPageFinished(url)
        view?.title?.let { onTitleChanged(it) }
    }

    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        // Proceed despite SSL errors (like the macOS app which doesn't block on cert issues)
        handler?.proceed()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: android.webkit.WebResourceError?
    ) {
        // Ignore resource load errors (often from blocked ad requests)
    }

    fun setCurrentUrl(url: String?) {
        currentUrl = url
    }
}
