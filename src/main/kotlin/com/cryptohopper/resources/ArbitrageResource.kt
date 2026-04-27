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
    public suspend fun exchangeStart(input: JsonObject): JsonElement? =
        transport.request("POST", "/arbitrage/exchange", body = input)

    public suspend fun exchangeCancel(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/arbitrage/cancel", body = input)

    public suspend fun exchangeResults(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/results", query = extra)

    public suspend fun exchangeHistory(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/history", query = extra)

    public suspend fun exchangeTotal(): JsonElement? = transport.request("GET", "/arbitrage/total")

    public suspend fun exchangeResetTotal(): JsonElement? =
        transport.request("POST", "/arbitrage/resettotal")

    public suspend fun marketStart(input: JsonObject): JsonElement? =
        transport.request("POST", "/arbitrage/market", body = input)

    public suspend fun marketCancel(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/arbitrage/market-cancel", body = input)

    public suspend fun marketResult(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/market-result", query = extra)

    public suspend fun marketHistory(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/market-history", query = extra)

    public suspend fun backlogs(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/arbitrage/get-backlogs", query = extra)

    public suspend fun backlog(id: Any): JsonElement? =
        transport.request("GET", "/arbitrage/get-backlog", query = mapOf("id" to id.toString()))

    public suspend fun deleteBacklog(id: Any): JsonElement? = transport.request(
        "POST", "/arbitrage/delete-backlog",
        body = buildJsonObject { put("id", id.toString()) },
    )
}
