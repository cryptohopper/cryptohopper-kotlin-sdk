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
    /** List AI assistant items / sessions. Requires `read`. */
    public suspend fun list(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/list", query = extra)

    /** Fetch a single AI item / session. Requires `read`. */
    public suspend fun get(id: Any): JsonElement? =
        transport.request("GET", "/ai/get", query = mapOf("id" to id.toString()))

    /** Models available to the authenticated user. */
    public suspend fun availableModels(): JsonElement? = transport.request("GET", "/ai/availablemodels")

    /** Remaining AI credit balance. Requires `read`. */
    public suspend fun getCredits(): JsonElement? = transport.request("GET", "/ai/getaicredits")

    /** Past invoices for AI-credit purchases. Requires `read`. */
    public suspend fun creditInvoices(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aicreditinvoices", query = extra)

    /** Credit spend/top-up transaction history. Requires `read`. */
    public suspend fun creditTransactions(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aicredittransactions", query = extra)

    /** Start a purchase of additional credits. Requires `user`. */
    public suspend fun buyCredits(input: JsonObject): JsonElement? =
        transport.request("POST", "/ai/buyaicredits", body = input)

    /** Options/metadata for the LLM analyse endpoint. Requires `read`. */
    public suspend fun llmAnalyzeOptions(): JsonElement? =
        transport.request("GET", "/ai/aillmanalyzeoptions")

    /** Run an LLM analysis. Requires `manage`. Usually async — returns a job id. */
    public suspend fun llmAnalyze(input: JsonObject): JsonElement? =
        transport.request("POST", "/ai/doaillmanalyze", body = input)

    /** Fetch the result(s) of an LLM analysis. Requires `read`. */
    public suspend fun llmAnalyzeResults(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aillmanalyzeresults", query = extra)

    /** Historical LLM analysis results. Requires `read`. */
    public suspend fun llmResults(extra: Map<String, String>? = null): JsonElement? =
        transport.request("GET", "/ai/aillmresults", query = extra)
}
