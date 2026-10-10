package com.salahu01.unadly

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.salahu01.unadly.data.PinStore
import com.salahu01.unadly.data.PinnedSite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BrowserViewModel(app: Application) : AndroidViewModel(app) {

    private val pinStore = PinStore(app)

    val currentUrl = MutableStateFlow<String?>(null)
    val currentTitle = MutableStateFlow("")
    val adsBlocked = MutableStateFlow(0)
    val isLoading = MutableStateFlow(false)
    val progress = MutableStateFlow(0)

    private val _pinnedSites = MutableStateFlow<List<PinnedSite>>(emptyList())
    val pinnedSites: StateFlow<List<PinnedSite>> = _pinnedSites.asStateFlow()

    val adHostCount = MutableStateFlow(0)

    init {
        loadPins()
        // Poll ad host loading
        viewModelScope.launch {
            while (true) {
                if (AdBlocker.isLoaded()) {
                    adHostCount.value = AdBlocker.adHostCount()
                    break
                }
                kotlinx.coroutines.delay(200)
            }
        }
    }

    fun loadPins() {
        _pinnedSites.value = pinStore.load()
    }

    fun pinCurrentSite() {
        val url = currentUrl.value ?: return
        val title = currentTitle.value.ifBlank { url }
        val favicon = UrlUtils.faviconUrl(url)
        pinStore.add(PinnedSite(url, title, favicon))
        loadPins()
    }

    fun unpinCurrentSite() {
        val url = currentUrl.value ?: return
        pinStore.remove(url)
        loadPins()
    }

    fun removePin(url: String) {
        pinStore.remove(url)
        loadPins()
    }

    fun isCurrentSitePinned(): Boolean {
        val url = currentUrl.value ?: return false
        return pinStore.isPinned(url)
    }

    fun onUrlChanged(url: String?) {
        currentUrl.value = url
    }

    fun onTitleChanged(title: String) {
        currentTitle.value = title
    }

    fun onAdsBlocked(count: Int) {
        adsBlocked.value = count
    }

    fun onLoadingChanged(loading: Boolean) {
        isLoading.value = loading
    }

    fun onProgress(prog: Int) {
        progress.value = prog
    }
}
