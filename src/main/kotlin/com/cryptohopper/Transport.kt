package com.cryptohopper

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Internal transport. Resources call this; SDK users go through [Client].
 *
 * Cryptohopper Public API v1 uses an `access-token: <token>` header rather
 * than the OAuth2-conventional `Authorization: Bearer <token>` — the gateway
 * in front of the API rejects Bearer with a SigV4 parse error. See
 * <https://www.cryptohopper.com/api-documentation/how-the-api-works> for the
 * authoritative reference.
 */
public class Transport internal constructor(
    private val apiKey: String,
    private val appKey: String?,
    private val baseUrl: String,
    private val maxRetries: Int,
    private val userAgent: String,
    private val httpClient: OkHttpClient,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /**
     * Perform a request against the Cryptohopper API. Auto-unwraps the
     * standard `{"data": ...}` envelope and retries on HTTP 429 up to
     * [maxRetries] times, honouring `Retry-After`.
     *
     * @return the JSON `data` field (typically a [JsonObject] or
     *         [kotlinx.serialization.json.JsonArray]). Returns [Unit] for
     *         204-style responses.
     */
    public suspend fun request(
        method: String,
        path: String,
        query: Map<String, String?>? = null,
        body: JsonElement? = null,
    ): JsonElement? {
        var attempt = 0
        while (true) {
            try {
                return doRequest(method, path, query, body)
            } catch (e: CryptohopperError) {
                if (e.code != "RATE_LIMITED" || attempt >= maxRetries) throw e
                val waitMs = e.retryAfterMs ?: (1000L * (1 shl attempt))
                delay(waitMs)
                attempt++
            }
        }
    }

    private suspend fun doRequest(
        method: String,
        path: String,
        query: Map<String, String?>?,
        body: JsonElement?,
    ): JsonElement? {
        val url = buildUrl(path, query)
        val builder = Request.Builder().url(url)
            .header("access-token", apiKey)
            .header("Accept", "application/json")
            .header("User-Agent", userAgent)
        if (!appKey.isNullOrEmpty()) {
            builder.header("x-api-app-key", appKey)
        }

        val payload = body?.let {
            it.toString().toRequestBody("application/json".toMediaType())
        }
        when (method.uppercase()) {
            "GET" -> builder.get()
            "POST" -> builder.post(payload ?: emptyBody())
            "PUT" -> builder.put(payload ?: emptyBody())
            "PATCH" -> builder.patch(payload ?: emptyBody())
            "DELETE" -> if (payload != null) builder.delete(payload) else builder.delete()
            else -> throw CryptohopperError(
                code = "VALIDATION_ERROR",
                message = "Unsupported HTTP method: $method",
                status = 0,
            )
        }

        val response = try {
            httpClient.newCall(builder.build()).await()
        } catch (e: SocketTimeoutException) {
            throw CryptohopperError("TIMEOUT", "Request timed out: ${e.message}", 0)
        } catch (e: IOException) {
            throw CryptohopperError(
                "NETWORK_ERROR",
                "Could not reach $baseUrl: ${e.message}",
                0,
            )
        }

        return response.use { handleResponse(it) }
    }

    private fun handleResponse(response: Response): JsonElement? {
        val status = response.code
        val raw = response.body?.string() ?: ""
        val parsed: JsonElement? = if (raw.isEmpty()) null else try {
            json.parseToJsonElement(raw)
        } catch (_: Exception) {
            null
        }

        if (status >= 400) {
            throw buildError(status, parsed, response)
        }

        // Unwrap {"data": ...} envelope when present.
        if (parsed is JsonObject && "data" in parsed) {
            return parsed["data"]
        }
        return parsed
    }

    private fun buildError(
        status: Int,
        parsed: JsonElement?,
        response: Response,
    ): CryptohopperError {
        val body = (parsed as? JsonObject) ?: emptyMap<String, JsonElement>()
        val message = (body["message"] as? JsonPrimitive)?.contentOrNull
            ?: "Request failed ($status)"
        val rawCode = (body["code"] as? JsonPrimitive)?.intOrNull
        val serverCode = if (rawCode != null && rawCode > 0) rawCode else null
        val ipAddress = (body["ip_address"] as? JsonPrimitive)?.contentOrNull
        val retryAfter = parseRetryAfter(response.header("Retry-After"))
        val code = defaultCodeForStatus(status)

        return CryptohopperError(
            code = code,
            message = message,
            status = status,
            serverCode = serverCode,
            ipAddress = ipAddress,
            retryAfterMs = retryAfter,
        )
    }

    private fun buildUrl(path: String, query: Map<String, String?>?): String {
        val full = if (path.startsWith("/")) path else "/$path"
        val urlBuilder = (baseUrl + full).toHttpUrl().newBuilder()
        query?.forEach { (k, v) -> if (v != null) urlBuilder.addQueryParameter(k, v) }
        return urlBuilder.build().toString()
    }

    private fun emptyBody() = "".toRequestBody("application/json".toMediaType())

    public companion object {
        public const val DEFAULT_BASE_URL: String = "https://api.cryptohopper.com/v1"
        public const val DEFAULT_TIMEOUT_SECONDS: Long = 30
        public const val DEFAULT_MAX_RETRIES: Int = 3

        private fun defaultCodeForStatus(status: Int): String = when (status) {
            400, 422 -> "VALIDATION_ERROR"
            401 -> "UNAUTHORIZED"
            402 -> "DEVICE_UNAUTHORIZED"
            403 -> "FORBIDDEN"
            404 -> "NOT_FOUND"
            409 -> "CONFLICT"
            429 -> "RATE_LIMITED"
            503 -> "SERVICE_UNAVAILABLE"
            in 500..599 -> "SERVER_ERROR"
            else -> "UNKNOWN"
        }

        private fun parseRetryAfter(header: String?): Long? {
            if (header.isNullOrEmpty()) return null
            return header.toLongOrNull()?.times(1000)
        }
    }
}

/**
 * Bridge between OkHttp's callback API and Kotlin coroutines. Cancelling
 * the surrounding scope cancels the in-flight call.
 */
internal suspend fun okhttp3.Call.await(): Response = suspendCoroutine { cont ->
    enqueue(object : okhttp3.Callback {
        override fun onResponse(call: okhttp3.Call, response: Response) {
            cont.resume(response)
        }

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            cont.resumeWithException(e)
        }
    })
}
