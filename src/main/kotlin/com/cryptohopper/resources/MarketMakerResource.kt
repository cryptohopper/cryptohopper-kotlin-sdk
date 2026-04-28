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
    /** Fetch the market-maker bot state for a hopper. Requires `read`. */
    public suspend fun get(hopperId: Any): JsonElement? =
        transport.request("GET", "/marketmaker/get", query = mapOf("hopper_id" to hopperId.toString()))

    /** Cancel running market-maker orders. Requires `trade`. */
    public suspend fun cancel(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/marketmaker/cancel", body = input)

    /** Historical order activity for a hopper's market maker. Requires `read`. */
    public suspend fun history(hopperId: Any, extra: Map<String, String>? = null): JsonElement? {
        val q = mutableMapOf<String, String?>("hopper_id" to hopperId.toString())
        extra?.forEach { (k, v) -> q[k] = v }
        return transport.request("GET", "/marketmaker/history", query = q)
    }

    /** Read the current market-trend override. Requires `read`. */
    public suspend fun getMarketTrend(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/marketmaker/get-market-trend", query = extra)

    /** Set a market-trend override. Requires `manage`. */
    public suspend fun setMarketTrend(input: JsonObject): JsonElement? =
        transport.request("POST", "/marketmaker/set-market-trend", body = input)

    /** Remove the current market-trend override. Requires `manage`. */
    public suspend fun deleteMarketTrend(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/marketmaker/delete-market-trend", body = input)

    /** List queued/pending market-maker backlog items. Requires `read`. */
    public suspend fun backlogs(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/marketmaker/get-backlogs", query = extra)

    /** Fetch a single backlog item by id. Requires `read`. */
    public suspend fun backlog(id: Any): JsonElement? =
        transport.request("GET", "/marketmaker/get-backlog", query = mapOf("id" to id.toString()))

    /** Delete a backlog item by id. Requires `manage`. */
    public suspend fun deleteBacklog(id: Any): JsonElement? = transport.request(
        "POST", "/marketmaker/delete-backlog",
        body = buildJsonObject { put("id", id.toString()) },
    )
}
