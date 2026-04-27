package com.cryptohopper.resources

import com.cryptohopper.Transport
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * `client.ai` — AI strategy assistance and credits.
 */
public class AIResource internal constructor(
    private val transport: Transport,
) {
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/list", query = extra)

    public suspend fun get(id: Any): JsonElement? =
        transport.request("GET", "/ai/get", query = mapOf("id" to id.toString()))

    public suspend fun availableModels(): JsonElement? = transport.request("GET", "/ai/availablemodels")

    public suspend fun getCredits(): JsonElement? = transport.request("GET", "/ai/getaicredits")

    public suspend fun creditInvoices(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aicreditinvoices", query = extra)

    public suspend fun creditTransactions(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aicredittransactions", query = extra)

    public suspend fun buyCredits(input: JsonObject): JsonElement? =
        transport.request("POST", "/ai/buyaicredits", body = input)

    public suspend fun llmAnalyzeOptions(): JsonElement? =
        transport.request("GET", "/ai/aillmanalyzeoptions")

    public suspend fun llmAnalyze(input: JsonObject): JsonElement? =
        transport.request("POST", "/ai/doaillmanalyze", body = input)

    public suspend fun llmAnalyzeResults(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aillmanalyzeresults", query = extra)

    public suspend fun llmResults(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aillmresults", query = extra)
}
