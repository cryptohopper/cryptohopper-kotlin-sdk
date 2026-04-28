package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.market` — marketplace browse: signals, marketplace items, homepage.
 */
public class MarketResource internal constructor(
    private val transport: Transport,
) {
    /** Browse marketplace signals. */
    public suspend fun signals(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/market/signals", query = extra)

    /** Fetch a single marketplace signal. */
    public suspend fun signal(signalId: Any): JsonElement? =
        transport.request("GET", "/market/signal", query = mapOf("signal_id" to signalId.toString()))

    /** Browse marketplace items (strategies, templates, signals, …). */
    public suspend fun items(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/market/marketitems", query = extra)

    /** Fetch a single marketplace item. */
    public suspend fun item(itemId: Any): JsonElement? =
        transport.request("GET", "/market/marketitem", query = mapOf("item_id" to itemId.toString()))

    /** Marketplace homepage (top picks, featured items). */
    public suspend fun homepage(): JsonElement? = transport.request("GET", "/market/homepage")
}
