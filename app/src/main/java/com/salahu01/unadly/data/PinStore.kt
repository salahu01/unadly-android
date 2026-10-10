package com.salahu01.unadly.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Pinned sites are stored in SharedPreferences as a JSON array.
 * Each entry: { "url": "https://example.com", "title": "Example", "faviconUrl": "https://example.com/favicon.ico" }
 */
data class PinnedSite(
    val url: String,
    val title: String,
    val faviconUrl: String
)

class PinStore(context: Context) {
    private val prefs = context.getSharedPreferences("unadly_pins", Context.MODE_PRIVATE)

    fun load(): List<PinnedSite> {
        val json = prefs.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(json)
        val list = mutableListOf<PinnedSite>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                PinnedSite(
                    url = o.getString("url"),
                    title = o.optString("title", o.getString("url")),
                    faviconUrl = o.optString("faviconUrl", "")
                )
            )
        }
        return list
    }

    fun add(site: PinnedSite) {
        val current = load().toMutableList()
        if (current.any { it.url == site.url }) return
        current.add(site)
        save(current)
    }

    fun remove(url: String) {
        val current = load().toMutableList()
        current.removeAll { it.url == url }
        save(current)
    }

    fun isPinned(url: String): Boolean = load().any { it.url == url }

    private fun save(list: List<PinnedSite>) {
        val arr = JSONArray()
        list.forEach { s ->
            arr.put(
                JSONObject().apply {
                    put("url", s.url)
                    put("title", s.title)
                    put("faviconUrl", s.faviconUrl)
                }
            )
        }
        prefs.edit().putString(KEY, arr.toString()).apply()
    }

    companion object {
        private const val KEY = "pinned_sites"
    }
}
