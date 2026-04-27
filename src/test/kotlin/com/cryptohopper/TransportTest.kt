package com.cryptohopper

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class TransportTest {
    private lateinit var server: MockWebServer

    @BeforeTest
    fun setUp() {
        server = MockWebServer().apply { start() }
    }

    @AfterTest
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `sends access-token header and unwraps data envelope`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{"id":42,"email":"alice@example.com"}}"""))

        val client = Client.builder()
            .apiKey("ch_test")
            .baseUrl(server.url("/v1").toString())
            .maxRetries(0)
            .build()

        val out = client.user.get()
        assertNotNull(out)

        val req = server.takeRequest()
        assertEquals("GET", req.method)
        assertEquals("ch_test", req.getHeader("access-token"))
        // The auth fix that ate hours of iter-52: must NOT send Bearer.
        assertEquals(null, req.getHeader("Authorization"))
        assertEquals("application/json", req.getHeader("Accept"))
        val ua = req.getHeader("User-Agent")
        assertNotNull(ua)
        check(ua.startsWith("cryptohopper-sdk-kotlin/"))
    }

    @Test
    fun `appKey is sent as x-api-app-key when set`() = runTest {
        server.enqueue(MockResponse().setBody("""{"data":{}}"""))
        val client = Client.builder()
            .apiKey("ch_test")
            .appKey("client_123")
            .baseUrl(server.url("/v1").toString())
            .maxRetries(0)
            .build()

        client.user.get()
        val req = server.takeRequest()
        assertEquals("client_123", req.getHeader("x-api-app-key"))
    }

    @Test
    fun `429 maps to RATE_LIMITED and retries when below maxRetries`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(429)
                .setHeader("Retry-After", "0")
                .setBody("""{"status":429,"code":1,"error":1,"message":"slow"}""")
        )
        server.enqueue(MockResponse().setBody("""{"data":{"id":1}}"""))

        val client = Client.builder()
            .apiKey("ch_test")
            .baseUrl(server.url("/v1").toString())
            .maxRetries(2)
            .build()

        val out = client.user.get()
        assertNotNull(out)
        assertEquals(2, server.requestCount)
    }

    @Test
    fun `403 maps to FORBIDDEN with ip_address populated`() = runTest {
        server.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody("""{"status":403,"code":0,"error":1,"message":"no access","ip_address":"1.2.3.4"}""")
        )
        val client = Client.builder()
            .apiKey("ch_test")
            .baseUrl(server.url("/v1").toString())
            .maxRetries(0)
            .build()

        val err = assertFailsWith<CryptohopperError> { client.user.get() }
        assertEquals("FORBIDDEN", err.code)
        assertEquals(403, err.status)
        assertEquals("1.2.3.4", err.ipAddress)
    }

    @Test
    fun `empty apiKey is rejected at build time`() {
        assertFailsWith<IllegalArgumentException> {
            Client.builder().apiKey("").build()
        }
    }
}
