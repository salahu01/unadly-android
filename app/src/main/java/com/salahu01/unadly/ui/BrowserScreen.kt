package com.salahu01.unadly.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.salahu01.unadly.WebViewFactory

@SuppressLint("RememberWebView")
@Composable
fun BrowserScreen(
    url: String,
    onBackToHome: () -> Unit,
    onPinnedChanged: () -> Unit,
    isPinned: Boolean,
    onPin: () -> Unit,
    onUnpin: () -> Unit,
    adsBlocked: Int,
    onAdsBlocked: (Int) -> Unit,
    onUrlChanged: (String?) -> Unit,
    onTitleChanged: (String) -> Unit
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var addressBarText by rememberSaveable { mutableStateOf(url) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top app bar with address bar and nav buttons
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                IconButton(
                    onClick = { webView?.goBack() },
                    enabled = canGoBack
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                // Forward button
                IconButton(
                    onClick = { webView?.goForward() },
                    enabled = canGoForward
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward")
                }

                // Address bar
                OutlinedTextField(
                    value = addressBarText,
                    onValueChange = { addressBarText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    singleLine = true,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
                    placeholder = { Text("Search or address") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            val normalized = com.salahu01.unadly.UrlUtils.normalize(addressBarText)
                            if (normalized.isNotEmpty()) {
                                webView?.loadUrl(normalized)
                            }
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                // Reload button
                IconButton(onClick = { webView?.reload() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reload")
                }

                // Pin/unpin button
                IconButton(onClick = {
                    if (isPinned) onUnpin() else onPin()
                }) {
                    Icon(
                        if (isPinned) Icons.Default.PushPin else Icons.Default.BookmarkBorder,
                        contentDescription = "Pin"
                    )
                }

                // Home button
                IconButton(onClick = onBackToHome) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            }
        }

        // Ad blocked counter
        if (adsBlocked > 0) {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🚫 $adsBlocked ads blocked on this page",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        // WebView
        androidx.compose.ui.viewinterop.AndroidView(
            factory = { ctx ->
                WebViewFactory.create(
                    context = ctx,
                    onAdsBlocked = { count ->
                        onAdsBlocked(count)
                    },
                    onUrlChanged = { u ->
                        onUrlChanged(u)
                        if (u != null) addressBarText = u
                    },
                    onTitleChanged = { t -> onTitleChanged(t) },
                    onPageStarted = { },
                    onPageFinished = { u ->
                        webView?.let { wv ->
                            canGoBack = wv.canGoBack()
                            canGoForward = wv.canGoForward()
                        }
                    },
                    onProgress = { }
                ).also { wv ->
                    wv.loadUrl(url)
                    webView = wv
                }
            },
            update = { wv ->
                webView = wv
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .navigationBarsPadding()
        )
    }
}


