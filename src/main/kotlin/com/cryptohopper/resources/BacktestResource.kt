package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.backtest` — submit and inspect backtests. Subject to the
 * `backtest` rate bucket (1 request per 2 seconds).
 */
public class BacktestResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun create(input: JsonObject): JsonElement? =
        transport.request("POST", "/backtest/new", body = input)

    public suspend fun get(backtestId: Any): JsonElement? =
        transport.request("GET", "/backtest/get", query = mapOf("backtest_id" to backtestId.toString()))

    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/backtest/list", query = extra)

    public suspend fun cancel(backtestId: Any): JsonElement? = transport.request(
        "POST", "/backtest/cancel",
        body = buildJsonObject { put("backtest_id", backtestId.toString()) },
    )

    public suspend fun restart(backtestId: Any): JsonElement? = transport.request(
        "POST", "/backtest/restart",
        body = buildJsonObject { put("backtest_id", backtestId.toString()) },
    )

    public suspend fun limits(): JsonElement? = transport.request("GET", "/backtest/limits")
}
