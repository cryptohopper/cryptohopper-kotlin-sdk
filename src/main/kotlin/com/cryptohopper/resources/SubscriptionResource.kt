package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * `client.subscription` — account-level subscription state, plans, credits.
 */
public class SubscriptionResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun hopper(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/subscription/hopper", query = extra)
    public suspend fun get(): JsonElement? = transport.request("GET", "/subscription/get")
    public suspend fun plans(): JsonElement? = transport.request("GET", "/subscription/plans")
    public suspend fun remap(input: JsonObject): JsonElement? =
        transport.request("POST", "/subscription/remap", body = input)
    public suspend fun assign(input: JsonObject): JsonElement? =
        transport.request("POST", "/subscription/assign", body = input)
    public suspend fun getCredits(): JsonElement? = transport.request("GET", "/subscription/getcredits")
    public suspend fun orderSub(input: JsonObject): JsonElement? =
        transport.request("POST", "/subscription/ordersub", body = input)
    public suspend fun stopSubscription(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/subscription/stopsubscription", body = input)
}
