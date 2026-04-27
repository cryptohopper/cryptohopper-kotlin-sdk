package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.webhooks` — developer webhook registration. Maps to the server's
 * `/api/webhook_*` endpoints; the resource name is `webhooks` for clarity.
 */
public class WebhooksResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun create(input: JsonObject): JsonElement? =
        transport.request("POST", "/api/webhook_create", body = input)

    public suspend fun delete(webhookId: Any): JsonElement? = transport.request(
        "POST", "/api/webhook_delete",
        body = buildJsonObject { put("webhook_id", webhookId.toString()) },
    )
}
