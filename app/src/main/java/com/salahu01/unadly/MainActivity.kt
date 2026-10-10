package com.salahu01.unadly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salahu01.unadly.ui.BrowserScreen
import com.salahu01.unadly.ui.HomeScreen
import com.salahu01.unadly.ui.theme.UnadlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnadlyTheme {
                UnadlyMainScreen()
            }
        }
    }
}

@Composable
fun UnadlyMainScreen() {
    val viewModel: BrowserViewModel = viewModel()
    val pinnedSites by viewModel.pinnedSites.collectAsState()
    val adsBlocked by viewModel.adsBlocked.collectAsState()
    val adHostCount by viewModel.adHostCount.collectAsState()
    val currentUrl by viewModel.currentUrl.collectAsState()
    val currentTitle by viewModel.currentTitle.collectAsState()

    var showHome by rememberSaveable { mutableStateOf(true) }
    var navigateUrl by rememberSaveable { mutableStateOf("") }

    if (showHome || navigateUrl.isEmpty()) {
        HomeScreen(
            pinnedSites = pinnedSites,
            adHostCount = adHostCount,
            onNavigate = { url ->
                navigateUrl = url
                showHome = false
            },
            onRemovePin = { url ->
                viewModel.removePin(url)
            }
        )
    } else {
        BrowserScreen(
            url = navigateUrl,
            onBackToHome = {
                showHome = true
                navigateUrl = ""
            },
            onPinnedChanged = { viewModel.loadPins() },
            isPinned = viewModel.isCurrentSitePinned(),
            onPin = { viewModel.pinCurrentSite() },
            onUnpin = { viewModel.unpinCurrentSite() },
            adsBlocked = adsBlocked,
            onAdsBlocked = { count -> viewModel.onAdsBlocked(count) },
            onUrlChanged = { url ->
                viewModel.onUrlChanged(url)
                if (url != null) navigateUrl = url
            },
            onTitleChanged = { title -> viewModel.onTitleChanged(title) }
        )
    }
}
