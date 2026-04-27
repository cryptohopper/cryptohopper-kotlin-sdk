package com.cryptohopper

import com.cryptohopper.resources.AIResource
import com.cryptohopper.resources.AppResource
import com.cryptohopper.resources.ArbitrageResource
import com.cryptohopper.resources.BacktestResource
import com.cryptohopper.resources.ChartResource
import com.cryptohopper.resources.ExchangeResource
import com.cryptohopper.resources.HoppersResource
import com.cryptohopper.resources.MarketResource
import com.cryptohopper.resources.MarketMakerResource
import com.cryptohopper.resources.PlatformResource
import com.cryptohopper.resources.SignalsResource
import com.cryptohopper.resources.SocialResource
import com.cryptohopper.resources.StrategyResource
import com.cryptohopper.resources.SubscriptionResource
import com.cryptohopper.resources.TemplateResource
import com.cryptohopper.resources.TournamentsResource
import com.cryptohopper.resources.UserResource
import com.cryptohopper.resources.WebhooksResource
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Asynchronous Cryptohopper API client.
 *
 * Construct once with [Builder] and reuse for the lifetime of your process —
 * the underlying OkHttp client maintains a connection pool. The object is
 * safe to share across coroutines.
 *
 * ```kotlin
 * val ch = Client.builder()
 *     .apiKey(System.getenv("CRYPTOHOPPER_TOKEN"))
 *     .timeout(30, TimeUnit.SECONDS)
 *     .build()
 *
 * runBlocking {
 *     val me = ch.user.get()
 *     val ticker = ch.exchange.ticker(exchange = "binance", market = "BTC/USDT")
 *     println("$me $ticker")
 * }
 * ```
 *
 * Covers all 18 public API domains (`user`, `hoppers`, `exchange`, `strategy`,
 * `backtest`, `market`, `signals`, `arbitrage`, `marketmaker`, `template`,
 * `ai`, `platform`, `chart`, `subscription`, `social`, `tournaments`,
 * `webhooks`, `app`) — same surface as every sibling SDK in the suite.
 */
public class Client private constructor(
    public val transport: Transport,
) {
    public val user: UserResource = UserResource(transport)
    public val hoppers: HoppersResource = HoppersResource(transport)
    public val exchange: ExchangeResource = ExchangeResource(transport)
    public val strategy: StrategyResource = StrategyResource(transport)
    public val backtest: BacktestResource = BacktestResource(transport)
    public val market: MarketResource = MarketResource(transport)
    public val signals: SignalsResource = SignalsResource(transport)
    public val arbitrage: ArbitrageResource = ArbitrageResource(transport)
    public val marketmaker: MarketMakerResource = MarketMakerResource(transport)
    public val template: TemplateResource = TemplateResource(transport)
    public val ai: AIResource = AIResource(transport)
    public val platform: PlatformResource = PlatformResource(transport)
    public val chart: ChartResource = ChartResource(transport)
    public val subscription: SubscriptionResource = SubscriptionResource(transport)
    public val social: SocialResource = SocialResource(transport)
    public val tournaments: TournamentsResource = TournamentsResource(transport)
    public val webhooks: WebhooksResource = WebhooksResource(transport)
    public val app: AppResource = AppResource(transport)

    /**
     * Builder for [Client].
     */
    public class Builder {
        private var apiKey: String? = null
        private var appKey: String? = null
        private var baseUrl: String = Transport.DEFAULT_BASE_URL
        private var timeoutSeconds: Long = Transport.DEFAULT_TIMEOUT_SECONDS
        private var maxRetries: Int = Transport.DEFAULT_MAX_RETRIES
        private var userAgentSuffix: String? = null
        private var httpClient: OkHttpClient? = null

        /** OAuth2 bearer token. Required. 40-char string from the developer dashboard. */
        public fun apiKey(value: String): Builder = apply { this.apiKey = value }

        /** Optional OAuth `client_id`, sent as the `x-api-app-key` header for per-app rate limits. */
        public fun appKey(value: String?): Builder = apply { this.appKey = value }

        /** Override API base URL (e.g. for staging). Defaults to https://api.cryptohopper.com/v1. */
        public fun baseUrl(value: String): Builder = apply { this.baseUrl = value }

        /** Per-request timeout. Defaults to 30 seconds. */
        public fun timeout(value: Long, unit: TimeUnit): Builder = apply {
            this.timeoutSeconds = unit.toSeconds(value)
        }

        /** Retries on HTTP 429 (respects `Retry-After`). 0 disables. Defaults to 3. */
        public fun maxRetries(value: Int): Builder = apply { this.maxRetries = value }

        /** Appended after `cryptohopper-sdk-kotlin/<version>` in the User-Agent header. */
        public fun userAgent(value: String): Builder = apply { this.userAgentSuffix = value }

        /** Bring your own OkHttp client (for proxies, custom CA, instrumentation, mTLS). */
        public fun httpClient(value: OkHttpClient): Builder = apply { this.httpClient = value }

        public fun build(): Client {
            val key = requireNotNull(apiKey) { "apiKey is required" }
            require(key.isNotEmpty()) { "apiKey must not be empty" }

            val ua = if (userAgentSuffix.isNullOrEmpty()) {
                "cryptohopper-sdk-kotlin/$SDK_VERSION"
            } else {
                "cryptohopper-sdk-kotlin/$SDK_VERSION $userAgentSuffix"
            }

            val ok = httpClient ?: OkHttpClient.Builder()
                .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .callTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .build()

            val transport = Transport(
                apiKey = key,
                appKey = appKey,
                baseUrl = baseUrl.trimEnd('/'),
                maxRetries = maxRetries,
                userAgent = ua,
                httpClient = ok,
            )
            return Client(transport)
        }
    }

    public companion object {
        /** Convenience: build a client with just an API key, defaults for everything else. */
        public fun create(apiKey: String): Client = Builder().apiKey(apiKey).build()

        /** Returns a fresh [Builder]. */
        public fun builder(): Builder = Builder()
    }
}
