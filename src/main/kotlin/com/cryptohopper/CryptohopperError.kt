package com.cryptohopper

/**
 * Single exception type raised by every SDK call on non-2xx responses and on
 * network/timeout failures. Unknown server-side codes pass through as raw
 * strings on [code] so callers can handle new codes without waiting for an
 * SDK update.
 *
 * Mirrors the shared error taxonomy across every official Cryptohopper SDK
 * (Node, Python, Go, Ruby, Rust, PHP, Dart, Swift). The [code] strings are
 * stable across SDKs — compare with `==`, never substring-match.
 */
public class CryptohopperError(
    /**
     * Machine-readable code from the shared SDK taxonomy. See [KNOWN_CODES].
     */
    public val code: String,
    message: String,
    /**
     * HTTP status code. Zero for transport-level failures.
     */
    public val status: Int,
    /**
     * Numeric `code` field from the Cryptohopper error envelope, when
     * present. Identifies the rate-limit bucket or other server-side
     * diagnostic code.
     */
    public val serverCode: Int? = null,
    /**
     * Caller IP as the server saw it, extracted from the error envelope's
     * `ip_address` field when present. Useful for debugging OAuth IP-allowlist
     * mismatches.
     */
    public val ipAddress: String? = null,
    /**
     * Milliseconds to wait before retrying, parsed from the `Retry-After`
     * header on a 429.
     */
    public val retryAfterMs: Long? = null,
) : RuntimeException(message) {

    override fun toString(): String {
        val extras = buildList {
            serverCode?.let { add("server_code=$it") }
            ipAddress?.let { add("ip=$it") }
            retryAfterMs?.let { add("retry_after_ms=$it") }
        }
        val extra = if (extras.isEmpty()) "" else " (${extras.joinToString(", ")})"
        return "CryptohopperError[$code/$status]$extra: $message"
    }

    public companion object {
        /**
         * The set of error codes the SDK emits. Server-side codes the SDK
         * doesn't recognise pass through verbatim on [code].
         */
        public val KNOWN_CODES: Set<String> = setOf(
            "VALIDATION_ERROR",
            "UNAUTHORIZED",
            "FORBIDDEN",
            "NOT_FOUND",
            "CONFLICT",
            "RATE_LIMITED",
            "SERVER_ERROR",
            "SERVICE_UNAVAILABLE",
            "DEVICE_UNAUTHORIZED",
            "NETWORK_ERROR",
            "TIMEOUT",
            "UNKNOWN",
        )
    }
}
