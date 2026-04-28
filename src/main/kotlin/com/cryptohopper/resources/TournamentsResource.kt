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
    /** List all tournaments (past, present, future). Requires `read`. */
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/gettournaments", query = extra)

    /** List currently-active tournaments. */
    public suspend fun active(): JsonElement? = transport.request("GET", "/tournaments/active")

    /** Fetch a single tournament. Requires `read`. */
    public suspend fun get(tournamentId: Any): JsonElement? = transport.request(
        "GET", "/tournaments/gettournament",
        query = mapOf("tournament_id" to tournamentId.toString()),
    )

    /** Search across tournaments. Requires `read`. */
    public suspend fun search(query: String): JsonElement? =
        transport.request("GET", "/tournaments/search", query = mapOf("query" to query))

    /** Trades placed inside a tournament. Requires `read`. */
    public suspend fun trades(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/trades", query = extra)

    /** Aggregated stats for a tournament. Requires `read`. */
    public suspend fun stats(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/stats", query = extra)

    /** Activity feed for a tournament. Requires `read`. */
    public suspend fun activity(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/activity", query = extra)

    /** Overall cross-tournament leaderboard. Requires `read`. */
    public suspend fun leaderboard(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/tournaments/leaderboard", query = extra)

    /** Leaderboard for a specific tournament. Requires `read`. */
    public suspend fun tournamentLeaderboard(tournamentId: Any): JsonElement? = transport.request(
        "GET", "/tournaments/leaderboard_tournament",
        query = mapOf("tournament_id" to tournamentId.toString()),
    )

    /** Join a tournament. Requires `manage`. */
    public suspend fun join(input: JsonObject): JsonElement? =
        transport.request("POST", "/tournaments/join", body = input)

    /** Leave a tournament. Requires `manage`. */
    public suspend fun leave(input: JsonObject): JsonElement? =
        transport.request("POST", "/tournaments/leave", body = input)
}
