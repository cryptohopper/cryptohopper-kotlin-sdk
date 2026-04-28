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
    /** Most recent Cryptohopper blog post. */
    public suspend fun latestBlog(): JsonElement? = transport.request("GET", "/platform/latestblog")

    /** Top-level documentation index. */
    public suspend fun documentation(): JsonElement? = transport.request("GET", "/platform/documentation")

    /** Current promotional banner content (shown on cryptohopper.com). */
    public suspend fun promoBar(): JsonElement? = transport.request("GET", "/platform/promobar")

    /** Full-text search across the docs site. */
    public suspend fun searchDocumentation(query: String): JsonElement? =
        transport.request("GET", "/platform/searchdocumentation", query = mapOf("query" to query))

    /** List of all known countries with ISO codes. */
    public suspend fun countries(): JsonElement? = transport.request("GET", "/platform/countries")

    /** List of countries the platform is currently available in. */
    public suspend fun countryAllowlist(): JsonElement? = transport.request("GET", "/platform/countryallowlist")

    /** The country the server resolved the caller's IP to. */
    public suspend fun ipCountry(): JsonElement? = transport.request("GET", "/platform/ipcountry")

    /** Supported UI languages with locale codes. */
    public suspend fun languages(): JsonElement? = transport.request("GET", "/platform/languages")

    /** Catalogue of bot types (DCA, grid, signal, market-maker, etc.). */
    public suspend fun botTypes(): JsonElement? = transport.request("GET", "/platform/bottypes")
}
