package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.hoppers` — bots ("hoppers") owned by the authenticated user.
 */
public class HoppersResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(exchange: String? = null): JsonElement? =
        transport.request("GET", "/hopper/list", query = exchange?.let { mapOf("exchange" to it) })

    public suspend fun get(hopperId: Any): JsonElement? =
        transport.request("GET", "/hopper/get", query = mapOf("hopper_id" to hopperId.toString()))

    public suspend fun create(input: JsonObject): JsonElement? =
        transport.request("POST", "/hopper/create", body = input)

    public suspend fun update(hopperId: Any, input: JsonObject): JsonElement? {
        val body = buildJsonObject {
            put("hopper_id", hopperId.toString())
            input.forEach { (k, v) -> put(k, v) }
        }
        return transport.request("POST", "/hopper/update", body = body)
    }

    public suspend fun delete(hopperId: Any): JsonElement? = transport.request(
        "POST", "/hopper/delete",
        body = buildJsonObject { put("hopper_id", hopperId.toString()) },
    )

    public suspend fun positions(hopperId: Any): JsonElement? =
        transport.request("GET", "/hopper/positions", query = mapOf("hopper_id" to hopperId.toString()))

    public suspend fun position(hopperId: Any, positionId: Any): JsonElement? =
        transport.request("GET", "/hopper/position", query = mapOf(
            "hopper_id" to hopperId.toString(),
            "position_id" to positionId.toString(),
        ))

    public suspend fun orders(hopperId: Any, extra: Map<String, String>? = null): JsonElement? {
        val q = mutableMapOf<String, String?>("hopper_id" to hopperId.toString())
        extra?.forEach { (k, v) -> q[k] = v }
        return transport.request("GET", "/hopper/orders", query = q)
    }

    public suspend fun buy(input: JsonObject): JsonElement? =
        transport.request("POST", "/hopper/buy", body = input)

    public suspend fun sell(input: JsonObject): JsonElement? =
        transport.request("POST", "/hopper/sell", body = input)

    public suspend fun configGet(hopperId: Any): JsonElement? =
        transport.request("GET", "/hopper/configget", query = mapOf("hopper_id" to hopperId.toString()))

    public suspend fun configUpdate(hopperId: Any, config: JsonObject): JsonElement? {
        val body = buildJsonObject {
            put("hopper_id", hopperId.toString())
            config.forEach { (k, v) -> put(k, v) }
        }
        return transport.request("POST", "/hopper/configupdate", body = body)
    }

    public suspend fun configPools(hopperId: Any): JsonElement? =
        transport.request("GET", "/hopper/configpools", query = mapOf("hopper_id" to hopperId.toString()))

    public suspend fun panic(hopperId: Any): JsonElement? = transport.request(
        "POST", "/hopper/panic",
        body = buildJsonObject { put("hopper_id", hopperId.toString()) },
    )
}
