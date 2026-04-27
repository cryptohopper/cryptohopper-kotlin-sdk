package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.user` — the authenticated user profile.
 */
public class UserResource internal constructor(
    private val transport: Transport,
) {
    /**
     * Fetch the authenticated user's profile.
     *
     * Returns the user's `id`, `email`, `username`, and other profile fields
     * as a [kotlinx.serialization.json.JsonObject]. The exact field set
     * depends on the user's plan and account configuration.
     */
    public suspend fun get(): JsonElement? = transport.request("GET", "/user")
}
