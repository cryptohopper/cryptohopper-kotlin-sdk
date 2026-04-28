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
    /** List the signals this provider has published. */
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/signals/signals", query = extra)

    /** Performance stats (winrate, avg profit per signal). */
    public suspend fun performance(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/signals/performance", query = extra)

    /** Overall provider stats (subscriber count, total PnL). */
    public suspend fun stats(): JsonElement? = transport.request("GET", "/signals/stats")

    /** Distribution of signals across exchanges / markets / types. */
    public suspend fun distribution(): JsonElement? = transport.request("GET", "/signals/distribution")

    /** Data series for charting the provider's performance over time. */
    public suspend fun chartData(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/signals/chartdata", query = extra)
}
