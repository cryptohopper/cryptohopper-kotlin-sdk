package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.strategy` — saved trading strategies.
 */
public class StrategyResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(): JsonElement? = transport.request("GET", "/strategy/strategies")

    public suspend fun get(strategyId: Any): JsonElement? = transport.request(
        "GET", "/strategy/get", query = mapOf("strategy_id" to strategyId.toString()),
    )

    public suspend fun create(input: JsonObject): JsonElement? =
        transport.request("POST", "/strategy/create", body = input)

    public suspend fun update(strategyId: Any, input: JsonObject): JsonElement? {
        val body = buildJsonObject {
            put("strategy_id", strategyId.toString())
            input.forEach { (k, v) -> put(k, v) }
        }
        return transport.request("POST", "/strategy/edit", body = body)
    }

    public suspend fun delete(strategyId: Any): JsonElement? = transport.request(
        "POST", "/strategy/delete",
        body = buildJsonObject { put("strategy_id", strategyId.toString()) },
    )
}
