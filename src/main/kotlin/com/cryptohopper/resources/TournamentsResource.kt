package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * `client.tournaments` — leaderboards, joinable tournaments, stats.
 */
public class TournamentsResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/gettournaments", query = extra)
    public suspend fun active(): JsonElement? = transport.request("GET", "/tournaments/active")
    public suspend fun get(tournamentId: Any): JsonElement? = transport.request(
        "GET", "/tournaments/gettournament",
        query = mapOf("tournament_id" to tournamentId.toString()),
    )
    public suspend fun search(query: String): JsonElement? =
        transport.request("GET", "/tournaments/search", query = mapOf("query" to query))
    public suspend fun trades(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/trades", query = extra)
    public suspend fun stats(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/stats", query = extra)
    public suspend fun activity(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/activity", query = extra)
    public suspend fun leaderboard(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/leaderboard", query = extra)
    public suspend fun tournamentLeaderboard(tournamentId: Any): JsonElement? = transport.request(
        "GET", "/tournaments/leaderboard_tournament",
        query = mapOf("tournament_id" to tournamentId.toString()),
    )
    public suspend fun join(input: JsonObject): JsonElement? =
        transport.request("POST", "/tournaments/join", body = input)
    public suspend fun leave(input: JsonObject): JsonElement? =
        transport.request("POST", "/tournaments/leave", body = input)
}
