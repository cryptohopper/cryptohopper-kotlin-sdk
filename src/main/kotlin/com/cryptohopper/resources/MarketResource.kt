package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.market` — marketplace browse: signals, marketplace items, homepage.
 */
public class MarketResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun signals(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/market/signals", query = extra)

    public suspend fun signal(signalId: Any): JsonElement? =
        transport.request("GET", "/market/signal", query = mapOf("signal_id" to signalId.toString()))

    public suspend fun items(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/market/marketitems", query = extra)

    public suspend fun item(itemId: Any): JsonElement? =
        transport.request("GET", "/market/marketitem", query = mapOf("item_id" to itemId.toString()))

    public suspend fun homepage(): JsonElement? = transport.request("GET", "/market/homepage")
}
