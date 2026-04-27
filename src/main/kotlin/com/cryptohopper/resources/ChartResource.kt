package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.chart` — saved chart configurations and shared snapshots.
 */
public class ChartResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(): JsonElement? = transport.request("GET", "/chart/list")
    public suspend fun get(chartId: Any): JsonElement? = transport.request(
        "GET", "/chart/get", query = mapOf("chart_id" to chartId.toString()),
    )
    public suspend fun save(input: JsonObject): JsonElement? =
        transport.request("POST", "/chart/save", body = input)
    public suspend fun delete(chartId: Any): JsonElement? = transport.request(
        "POST", "/chart/delete",
        body = buildJsonObject { put("chart_id", chartId.toString()) },
    )
    public suspend fun shareSave(input: JsonObject): JsonElement? =
        transport.request("POST", "/chart/share-save", body = input)
    public suspend fun shareGet(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/chart/share-get", query = extra)
}
