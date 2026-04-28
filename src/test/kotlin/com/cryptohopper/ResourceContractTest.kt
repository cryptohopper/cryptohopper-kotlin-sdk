package com.cryptohopper

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Verifies that every resource method that takes a *required* identifier
 * sends it as the right query/body key under the right URL path.
 *
 * Locks in the contract for the 7 methods fixed in iter 69 (where required
 * params were previously typed as optional `Map<String, String>?`) plus a
 * handful of high-traffic methods so that an accidental signature change
 * trips CI.
 */
class ResourceContractTest {
    private lateinit var server: MockWebServer

    @BeforeTest
    fun setUp() {
        server = MockWebServer().apply { start() }
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    private fun client(): Client = Client.builder()
        .apiKey("ch_test")
        .baseUrl(server.url("/v1").toString())
        .maxRetries(0)
        .build()

    // ── subscription.hopper ───────────────────────────────────────────────

    @Test
    fun `subscription_hopper sends hopper_id query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        client().subscription.hopper(42)
        val req = server.takeRequest()
        assertEquals("GET", req.method)
        assertEquals("/v1/subscription/hopper?hopper_id=42", req.path)
    }

    // ── social.getConversation ────────────────────────────────────────────

    @Test
    fun `social_getConversation sends conversation_id query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":[]}"""))
        client().social.getConversation(7)
        val req = server.takeRequest()
        assertEquals("GET", req.method)
        assertEquals("/v1/social/loadconversation?conversation_id=7", req.path)
    }

    // ── social.getComments ────────────────────────────────────────────────

    @Test
    fun `social_getComments sends post_id query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":[]}"""))
        client().social.getComments(123)
        val req = server.takeRequest()
        assertEquals("GET", req.method)
        assertEquals("/v1/social/getcomments?post_id=123", req.path)
    }

    // ── social.getMedia ───────────────────────────────────────────────────

    @Test
    fun `social_getMedia sends media_id query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        client().social.getMedia(99)
        val req = server.takeRequest()
        assertEquals("GET", req.method)
        assertEquals("/v1/social/getmedia?media_id=99", req.path)
    }

    // ── social.getFollowers / getFollowing / getFollowingProfiles ─────────

    @Test
    fun `social_getFollowers sends alias query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":[]}"""))
        client().social.getFollowers("alice")
        val req = server.takeRequest()
        assertEquals("/v1/social/followers?alias=alice", req.path)
    }

    @Test
    fun `social_getFollowing sends alias query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        client().social.getFollowing("alice")
        val req = server.takeRequest()
        assertEquals("/v1/social/following?alias=alice", req.path)
    }

    @Test
    fun `social_getFollowingProfiles sends alias query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":[]}"""))
        client().social.getFollowingProfiles("alice")
        val req = server.takeRequest()
        assertEquals("/v1/social/followingprofiles?alias=alice", req.path)
    }

    // ── A few high-traffic methods, locked in for parity ──────────────────

    @Test
    fun `hoppers_get sends hopper_id query`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        client().hoppers.get(42)
        val req = server.takeRequest()
        assertEquals("/v1/hopper/get?hopper_id=42", req.path)
    }

    @Test
    fun `hoppers_panic posts hopper_id body`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        client().hoppers.panic(42)
        val req = server.takeRequest()
        assertEquals("POST", req.method)
        assertEquals("/v1/hopper/panic", req.path)
        val body = req.body.readUtf8()
        // body keys are unordered in JSON, just check the content
        assert(body.contains("\"hopper_id\""))
        assert(body.contains("\"42\""))
    }

    @Test
    fun `exchange_ticker sends both exchange and market`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        client().exchange.ticker(exchange = "binance", market = "BTC/USDT")
        val req = server.takeRequest()
        assertEquals("GET", req.method)
        val path = req.path ?: ""
        assert(path.startsWith("/v1/exchange/ticker?")) { "actual: $path" }
        assert(path.contains("exchange=binance")) { "actual: $path" }
        // "/" inside the value may render as "/" or "%2F" depending on the
        // URL builder — accept either rather than over-specify.
        assert(path.contains("market=BTC/USDT") || path.contains("market=BTC%2FUSDT")) {
            "actual: $path"
        }
    }

    @Test
    fun `backtest_create posts JSON body verbatim`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{"id":1}}"""))
        val payload: JsonObject = buildJsonObject {
            put("hopper_id", 42)
            put("start_date", "2026-01-01")
            put("end_date", "2026-04-01")
        }
        client().backtest.create(payload)
        val req = server.takeRequest()
        assertEquals("POST", req.method)
        assertEquals("/v1/backtest/new", req.path)
        val body = req.body.readUtf8()
        assert(body.contains("\"hopper_id\":42"))
        assert(body.contains("\"start_date\":\"2026-01-01\""))
    }
}
