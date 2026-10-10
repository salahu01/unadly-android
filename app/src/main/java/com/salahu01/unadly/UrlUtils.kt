package com.salahu01.unadly

/**
 * Utilities for URL handling.
 */
object UrlUtils {

    /**
     * If [input] looks like a URL (has a dot, no spaces), returns the https:// URL.
     * Otherwise returns a DuckDuckGo search URL.
     */
    fun normalize(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return ""
        // Already has a scheme
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed
        }
        // Looks like a URL: has a dot and no spaces
        if (trimmed.contains(".") && !trimmed.contains(" ")) {
            return "https://$trimmed"
        }
        // Otherwise, search
        return "https://duckduckgo.com/?q=" + java.net.URLEncoder.encode(trimmed, "UTF-8")
    }

    /**
     * Extracts the host from a URL string, or null if it can't be parsed.
     */
    fun host(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return try {
            val u = java.net.URI(url)
            u.host?.lowercase()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Returns the root/registrable domain from a host (best-effort).
     * e.g. "ads.example.com" -> "example.com"
     */
    fun rootDomain(host: String): String {
        val parts = host.split('.')
        return if (parts.size > 2) parts.takeLast(2).joinToString(".") else host
    }

    /**
     * Returns a favicon URL for the given site URL.
     */
    fun faviconUrl(siteUrl: String): String {
        val h = host(siteUrl) ?: return ""
        return "https://$h/favicon.ico"
    }
}
