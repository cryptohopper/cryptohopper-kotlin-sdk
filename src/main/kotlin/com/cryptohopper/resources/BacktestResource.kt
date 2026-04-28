package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.backtest` — submit and inspect backtests. Subject to the
 * `backtest` rate bucket (1 request per 2 seconds).
 *
 * Backtests run async server-side. [create] returns immediately with an ID;
 * poll [get] until `status` is `"completed"` or `"failed"`.
 */
public class BacktestResource internal constructor(
    private val transport: Transport,
) {
    /** Start a new backtest. Returns immediately with an ID; poll [get] until terminal. Requires `manage`. */
    public suspend fun create(input: JsonObject): JsonElement? =
        transport.request("POST", "/backtest/new", body = input)

    /** Fetch a backtest by ID. Status is `"completed"`, `"failed"`, or in-flight. Requires `read`. */
    public suspend fun get(backtestId: Any): JsonElement? =
        transport.request("GET", "/backtest/get", query = mapOf("backtest_id" to backtestId.toString()))

    /** List backtests. Requires `read`. */
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/backtest/list", query = extra)

    /** Cancel a running backtest. Requires `manage`. */
    public suspend fun cancel(backtestId: Any): JsonElement? = transport.request(
        "POST", "/backtest/cancel",
        body = buildJsonObject { put("backtest_id", backtestId.toString()) },
    )

    /** Restart a backtest with the same parameters. Requires `manage`. */
    public suspend fun restart(backtestId: Any): JsonElement? = transport.request(
        "POST", "/backtest/restart",
        body = buildJsonObject { put("backtest_id", backtestId.toString()) },
    )

    /** Get the user's remaining backtest quota for the current period. Requires `read`. */
    public suspend fun limits(): JsonElement? = transport.request("GET", "/backtest/limits")
}
