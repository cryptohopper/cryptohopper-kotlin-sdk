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
    /** Subscription state for a specific hopper. */
    public suspend fun hopper(hopperId: Any): JsonElement? =
        transport.request("GET", "/subscription/hopper", query = mapOf("hopper_id" to hopperId.toString()))

    /** Account-level subscription state (plan, slot count, billing). */
    public suspend fun get(): JsonElement? = transport.request("GET", "/subscription/get")

    /** List available subscription plans. */
    public suspend fun plans(): JsonElement? = transport.request("GET", "/subscription/plans")

    /** Move a subscription slot from one hopper to another. */
    public suspend fun remap(input: JsonObject): JsonElement? =
        transport.request("POST", "/subscription/remap", body = input)

    /** Assign a subscription slot to a hopper. */
    public suspend fun assign(input: JsonObject): JsonElement? =
        transport.request("POST", "/subscription/assign", body = input)

    /** Remaining platform credits on the account. */
    public suspend fun getCredits(): JsonElement? = transport.request("GET", "/subscription/getcredits")

    /** Start a subscription purchase (returns a payment URL or token). */
    public suspend fun orderSub(input: JsonObject): JsonElement? =
        transport.request("POST", "/subscription/ordersub", body = input)

    /** Cancel an active subscription. */
    public suspend fun stopSubscription(input: JsonObject = JsonObject(emptyMap())): JsonElement? =
        transport.request("POST", "/subscription/stopsubscription", body = input)
}
