package com.salahu01.unadly

import android.content.Context
import java.util.concurrent.ConcurrentHashMap

/**
 * Loads the ad-server host list from assets/adhosts.txt into a HashSet
 * and provides O(1) lookups. Blocking follows the macOS approach:
 * a request is blocked only if it is third-party (host differs from the
 * page host) AND the request host (or any parent domain) is in the set.
 */
object AdBlocker {

    private val adHosts: MutableSet<String> = ConcurrentHashMap.newKeySet()
    @Volatile private var loaded = false

    fun init(context: Context) {
        if (loaded) return
        Thread {
            val start = System.currentTimeMillis()
            val set = HashSet<String>(45000)
            context.assets.open("adhosts.txt").bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    val trimmed = line.trim()
                    if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
                        set.add(trimmed.lowercase())
                    }
                }
            }
            adHosts.clear()
            adHosts.addAll(set)
            loaded = true
            android.util.Log.i("AdBlocker", "Loaded ${set.size} ad hosts in ${System.currentTimeMillis() - start}ms")
        }.start()
    }

    fun isLoaded(): Boolean = loaded

    fun adHostCount(): Int = adHosts.size

    /**
     * Returns true if the request should be blocked.
     * [requestHost] is the host of the resource being requested.
     * [pageHost] is the host of the page the user is on (null on home/blank).
     *
     * Blocks only third-party requests: if pageHost is null, we skip blocking.
     * Otherwise, if the request host differs from the page host and the
     * request host (or any parent domain) is in the ad-host set, block it.
     */
    fun shouldBlock(requestHost: String?, pageHost: String?): Boolean {
        if (!loaded || requestHost.isNullOrBlank() || pageHost.isNullOrBlank()) return false
        val req = requestHost.lowercase()
        val page = pageHost.lowercase()

        // First-party: same host or subdomain of page host -> allow
        if (req == page || req.endsWith(".$page")) return false

        // Third-party: check if request host or any parent domain is in the set
        var host = req
        while (host.isNotEmpty()) {
            if (host in adHosts) return true
            val dot = host.indexOf('.')
            if (dot < 0) break
            host = host.substring(dot + 1)
        }
        return false
    }
}
