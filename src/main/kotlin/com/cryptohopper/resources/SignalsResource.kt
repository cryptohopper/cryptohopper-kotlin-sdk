package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.signals` — signal-provider analytics. Distinct from
 * [MarketResource.signals], which browses the marketplace.
 */
public class SignalsResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/signals/signals", query = extra)

    public suspend fun performance(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/signals/performance", query = extra)

    public suspend fun stats(): JsonElement? = transport.request("GET", "/signals/stats")

    public suspend fun distribution(): JsonElement? = transport.request("GET", "/signals/distribution")

    public suspend fun chartData(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/signals/chartdata", query = extra)
}
