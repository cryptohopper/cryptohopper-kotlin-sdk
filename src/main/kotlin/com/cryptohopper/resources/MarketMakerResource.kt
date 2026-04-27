package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.marketmaker` — market-making bot inspection and configuration.
 */
public class MarketMakerResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun get(hopperId: Any): JsonElement? =
        transport.request("GET", "/marketmaker/get", query = mapOf("hopper_id" to hopperId.toString()))

    public suspend fun cancel(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/marketmaker/cancel", body = input)

    public suspend fun history(hopperId: Any, extra: Map<String, String>? = null): JsonElement? {
        val q = mutableMapOf<String, String?>("hopper_id" to hopperId.toString())
        extra?.forEach { (k, v) -> q[k] = v }
        return transport.request("GET", "/marketmaker/history", query = q)
    }

    public suspend fun getMarketTrend(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/marketmaker/get-market-trend", query = extra)

    public suspend fun setMarketTrend(input: JsonObject): JsonElement? =
        transport.request("POST", "/marketmaker/set-market-trend", body = input)

    public suspend fun deleteMarketTrend(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/marketmaker/delete-market-trend", body = input)

    public suspend fun backlogs(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/marketmaker/get-backlogs", query = extra)

    public suspend fun backlog(id: Any): JsonElement? =
        transport.request("GET", "/marketmaker/get-backlog", query = mapOf("id" to id.toString()))

    public suspend fun deleteBacklog(id: Any): JsonElement? = transport.request(
        "POST", "/marketmaker/delete-backlog",
        body = buildJsonObject { put("id", id.toString()) },
    )
}
