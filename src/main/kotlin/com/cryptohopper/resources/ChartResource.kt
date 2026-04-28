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
    /** List the user's saved chart configurations. */
    public suspend fun list(): JsonElement? = transport.request("GET", "/chart/list")

    /** Fetch a saved chart by ID. */
    public suspend fun get(chartId: Any): JsonElement? = transport.request(
        "GET", "/chart/get", query = mapOf("chart_id" to chartId.toString()),
    )

    /** Save a new chart configuration. */
    public suspend fun save(input: JsonObject): JsonElement? =
        transport.request("POST", "/chart/save", body = input)

    /** Delete a saved chart by ID. */
    public suspend fun delete(chartId: Any): JsonElement? = transport.request(
        "POST", "/chart/delete",
        body = buildJsonObject { put("chart_id", chartId.toString()) },
    )

    /** Save a shared snapshot of a chart for distribution. */
    public suspend fun shareSave(input: JsonObject): JsonElement? =
        transport.request("POST", "/chart/share-save", body = input)

    /** Fetch a shared chart snapshot. */
    public suspend fun shareGet(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/chart/share-get", query = extra)
}
