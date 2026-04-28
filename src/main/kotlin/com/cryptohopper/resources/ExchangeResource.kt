package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.exchange` — exchange-level market data (tickers, candles, orderbook).
 *
 * Although these endpoints are conceptually "public market data," the AWS API
 * Gateway in front of the production API still requires a valid OAuth bearer
 * on every call. Pass a real token to the [com.cryptohopper.Client] regardless.
 */
public class ExchangeResource internal constructor(
    private val transport: Transport,
) {
    /** Current ticker for a market on an exchange. */
    public suspend fun ticker(exchange: String, market: String): JsonElement? =
        transport.request("GET", "/exchange/ticker",
            query = mapOf("exchange" to exchange, "market" to market))

    /** OHLCV candles for a market. Timeframe is e.g. "1m", "5m", "1h", "1d". */
    public suspend fun candles(
        exchange: String,
        market: String,
        timeframe: String,
        from: Long? = null,
        to: Long? = null,
    ): JsonElement? = transport.request("GET", "/exchange/candle", query = mapOf(
        "exchange" to exchange,
        "market" to market,
        "timeframe" to timeframe,
        "from" to from?.toString(),
        "to" to to?.toString(),
    ))

    /** Order book depth for a market on an exchange. */
    public suspend fun orderbook(exchange: String, market: String): JsonElement? =
        transport.request("GET", "/exchange/orderbook",
            query = mapOf("exchange" to exchange, "market" to market))

    /** List markets available on an exchange. */
    public suspend fun markets(exchange: String): JsonElement? =
        transport.request("GET", "/exchange/markets", query = mapOf("exchange" to exchange))

    /** List currencies available on an exchange. */
    public suspend fun currencies(exchange: String): JsonElement? =
        transport.request("GET", "/exchange/currencies", query = mapOf("exchange" to exchange))

    /** List all supported exchanges. */
    public suspend fun exchanges(): JsonElement? = transport.request("GET", "/exchange/exchanges")

    /** Fiat forex rates used for currency conversion. */
    public suspend fun forexRates(): JsonElement? = transport.request("GET", "/exchange/forex-rates")
}
