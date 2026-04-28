package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.arbitrage` — exchange and market arbitrage helpers.
 */
public class ArbitrageResource internal constructor(
    private val transport: Transport,
) {
    /** Start a cross-exchange arbitrage run. Requires `trade`. */
    public suspend fun exchangeStart(input: JsonObject): JsonElement? =
        transport.request("POST", "/arbitrage/exchange", body = input)

    /** Cancel a cross-exchange arbitrage run. Requires `trade`. */
    public suspend fun exchangeCancel(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/arbitrage/cancel", body = input)

    /** Fetch results of exchange-arbitrage runs. Requires `read`. */
    public suspend fun exchangeResults(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/results", query = extra)

    /** Historical exchange-arbitrage runs. Requires `read`. */
    public suspend fun exchangeHistory(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/history", query = extra)

    /** Aggregated totals across all exchange-arbitrage runs. Requires `read`. */
    public suspend fun exchangeTotal(): JsonElement? = transport.request("GET", "/arbitrage/total")

    /** Reset the running totals. Requires `manage`. */
    public suspend fun exchangeResetTotal(): JsonElement? =
        transport.request("POST", "/arbitrage/resettotal")

    /** Start an intra-exchange (e.g. triangular) arbitrage run. Requires `trade`. */
    public suspend fun marketStart(input: JsonObject): JsonElement? =
        transport.request("POST", "/arbitrage/market", body = input)

    /** Cancel a market-arbitrage run. Requires `trade`. */
    public suspend fun marketCancel(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/arbitrage/market-cancel", body = input)

    /** Result of a specific market-arbitrage run. Requires `read`. */
    public suspend fun marketResult(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/market-result", query = extra)

    /** Historical market-arbitrage runs. Requires `read`. */
    public suspend fun marketHistory(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/market-history", query = extra)

    /** List queued/pending arbitrage backlog items. Requires `read`. */
    public suspend fun backlogs(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/get-backlogs", query = extra)

    /** Fetch a single backlog item by id. Requires `read`. */
    public suspend fun backlog(id: Any): JsonElement? =
        transport.request("GET", "/arbitrage/get-backlog", query = mapOf("id" to id.toString()))

    /** Delete a backlog item by id. Requires `manage`. */
    public suspend fun deleteBacklog(id: Any): JsonElement? = transport.request(
        "POST", "/arbitrage/delete-backlog",
        body = buildJsonObject { put("id", id.toString()) },
    )
}
