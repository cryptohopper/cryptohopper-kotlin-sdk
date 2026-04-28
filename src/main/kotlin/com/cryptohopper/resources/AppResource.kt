package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * `client.app` — mobile app store: receipt validation, in-app purchases.
 *
 * Primarily used by Cryptohopper's own mobile apps. Most third-party
 * integrations don't need this resource.
 */
public class AppResource internal constructor(
    private val transport: Transport,
) {
    /** Validate an App Store / Play Store receipt. */
    public suspend fun receipt(input: JsonObject): JsonElement? =
        transport.request("POST", "/app/receipt", body = input)

    /** Record an in-app purchase. */
    public suspend fun inAppPurchase(input: JsonObject): JsonElement? =
        transport.request("POST", "/app/in_app_purchase", body = input)
}
