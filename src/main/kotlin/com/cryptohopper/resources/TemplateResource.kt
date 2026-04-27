package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * `client.template` — saved bot-config templates.
 */
public class TemplateResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(): JsonElement? = transport.request("GET", "/template/templates")

    public suspend fun get(templateId: Any): JsonElement? = transport.request(
        "GET", "/template/get", query = mapOf("template_id" to templateId.toString()),
    )

    public suspend fun basic(templateId: Any): JsonElement? = transport.request(
        "GET", "/template/basic", query = mapOf("template_id" to templateId.toString()),
    )

    public suspend fun save(input: JsonObject): JsonElement? =
        transport.request("POST", "/template/save-template", body = input)

    public suspend fun update(input: JsonObject): JsonElement? =
        transport.request("POST", "/template/update", body = input)

    public suspend fun load(templateId: Any, hopperId: Any): JsonElement? = transport.request(
        "POST", "/template/load",
        body = buildJsonObject {
            put("template_id", templateId.toString())
            put("hopper_id", hopperId.toString())
        },
    )

    public suspend fun delete(templateId: Any): JsonElement? = transport.request(
        "POST", "/template/delete",
        body = buildJsonObject { put("template_id", templateId.toString()) },
    )
}
