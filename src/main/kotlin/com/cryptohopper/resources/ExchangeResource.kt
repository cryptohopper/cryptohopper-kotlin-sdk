package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement

/**
 * `client.exchange` — exchange-level market data (tickers, candles, orderbook).
 */
public class ExchangeResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun ticker(exchange: String, market: String): JsonElement? =
        transport.request("GET", "/exchange/ticker",
            query = mapOf("exchange" to exchange, "market" to market))

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

    public suspend fun orderbook(exchange: String, market: String): JsonElement? =
        transport.request("GET", "/exchange/orderbook",
            query = mapOf("exchange" to exchange, "market" to market))

    public suspend fun markets(exchange: String): JsonElement? =
        transport.request("GET", "/exchange/markets", query = mapOf("exchange" to exchange))

    public suspend fun currencies(exchange: String): JsonElement? =
        transport.request("GET", "/exchange/currencies", query = mapOf("exchange" to exchange))

    public suspend fun exchanges(): JsonElement? = transport.request("GET", "/exchange/exchanges")

    public suspend fun forexRates(): JsonElement? = transport.request("GET", "/exchange/forex-rates")
}
