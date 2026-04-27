package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.platform` — platform-level metadata: blog posts, supported
 * countries, languages, bot types, etc.
 */
public class PlatformResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun latestBlog(): JsonElement? = transport.request("GET", "/platform/latestblog")
    public suspend fun documentation(): JsonElement? = transport.request("GET", "/platform/documentation")
    public suspend fun promoBar(): JsonElement? = transport.request("GET", "/platform/promobar")
    public suspend fun searchDocumentation(query: String): JsonElement? =
        transport.request("GET", "/platform/searchdocumentation", query = mapOf("query" to query))
    public suspend fun countries(): JsonElement? = transport.request("GET", "/platform/countries")
    public suspend fun countryAllowlist(): JsonElement? = transport.request("GET", "/platform/countryallowlist")
    public suspend fun ipCountry(): JsonElement? = transport.request("GET", "/platform/ipcountry")
    public suspend fun languages(): JsonElement? = transport.request("GET", "/platform/languages")
    public suspend fun botTypes(): JsonElement? = transport.request("GET", "/platform/bottypes")
}
