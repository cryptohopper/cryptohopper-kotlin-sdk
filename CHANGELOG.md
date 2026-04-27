# Changelog

All notable changes to `com.cryptohopper:cryptohopper` are documented here.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and
the project adheres to [Semantic Versioning](https://semver.org/).

## 0.1.0-alpha.1 — Unreleased

Initial alpha release. Full coverage of all 18 public API domains from day one. Matches the feature surface of the other official SDKs (Node/Python/Go/Ruby/Rust/PHP/Dart/Swift) at their current alpha versions.

### Added

#### Transport

- **`com.cryptohopper.Client`** — async client built on OkHttp with a fluent `Client.Builder`. `Client.create(apiKey)` shortcut for the simple case.
- **`CryptohopperError`** — single exception type whose `code` follows the shared SDK taxonomy (`UNAUTHORIZED`, `FORBIDDEN`, `NOT_FOUND`, `RATE_LIMITED`, `VALIDATION_ERROR`, `DEVICE_UNAUTHORIZED`, `CONFLICT`, `SERVER_ERROR`, `SERVICE_UNAVAILABLE`, `NETWORK_ERROR`, `TIMEOUT`, `UNKNOWN`). Carries `code`, `status`, `serverCode`, `ipAddress`, `retryAfterMs`. Stable across SDKs.
- **`Transport`** — internal HTTP layer. Auto-retries 429 honouring `Retry-After`, auto-unwraps the `{"data": ...}` envelope, maps every error path to `CryptohopperError`. Sends the v1 API's `access-token` header (not the OAuth2-conventional `Authorization: Bearer` — see [why](https://www.cryptohopper.com/api-documentation/how-the-api-works)).
- **`x-api-app-key`** support via `Builder.appKey(...)` for per-app rate-limit attribution.
- **Custom OkHttp client injection** via `Builder.httpClient(...)` for proxies, mTLS, instrumentation.
- **Configurable timeout, max-retries, base URL** through the builder.

#### Resources

All 18 public API domains:

- **`user`** — `get`
- **`hoppers`** — `list`, `get`, `create`, `update`, `delete`, `positions`, `position`, `orders`, `buy`, `sell`, `configGet`, `configUpdate`, `configPools`, `panic`
- **`exchange`** — `ticker`, `candles`, `orderbook`, `markets`, `currencies`, `exchanges`, `forexRates`
- **`strategy`** — `list`, `get`, `create`, `update`, `delete`
- **`backtest`** — `create`, `get`, `list`, `cancel`, `restart`, `limits`
- **`market`** — `signals`, `signal`, `items`, `item`, `homepage`
- **`signals`** — `list`, `performance`, `stats`, `distribution`, `chartData` (signal-provider analytics; distinct from `market.signals`)
- **`arbitrage`** — `exchangeStart`, `exchangeCancel`, `exchangeResults`, `exchangeHistory`, `exchangeTotal`, `exchangeResetTotal`, `marketStart`, `marketCancel`, `marketResult`, `marketHistory`, `backlogs`, `backlog`, `deleteBacklog`
- **`marketmaker`** — `get`, `cancel`, `history`, `getMarketTrend`, `setMarketTrend`, `deleteMarketTrend`, `backlogs`, `backlog`, `deleteBacklog`
- **`template`** — `list`, `get`, `basic`, `save`, `update`, `load`, `delete`
- **`ai`** — `list`, `get`, `availableModels`, `getCredits`, `creditInvoices`, `creditTransactions`, `buyCredits`, `llmAnalyzeOptions`, `llmAnalyze`, `llmAnalyzeResults`, `llmResults`
- **`platform`** — `latestBlog`, `documentation`, `promoBar`, `searchDocumentation`, `countries`, `countryAllowlist`, `ipCountry`, `languages`, `botTypes`
- **`chart`** — `list`, `get`, `save`, `delete`, `shareSave`, `shareGet`
- **`subscription`** — `hopper`, `get`, `plans`, `remap`, `assign`, `getCredits`, `orderSub`, `stopSubscription`
- **`social`** — 27 methods (profiles, feed, trends, search, notifications, conversations/messages, posts, comments, media, follows, likes/reposts, moderation)
- **`tournaments`** — `list`, `active`, `get`, `search`, `trades`, `stats`, `activity`, `leaderboard`, `tournamentLeaderboard`, `join`, `leave`
- **`webhooks`** — `create`, `delete` (developer webhook registration)
- **`app`** — `receipt`, `inAppPurchase` (mobile app store, primarily internal)

#### Tests
- 5 transport-level tests using OkHttp's `MockWebServer`: access-token header presence, `Authorization` *absence*, app-key forwarding, 429 retry honouring `Retry-After`, 403 with `ip_address`, empty-apiKey rejection at build time.

### Why a Kotlin SDK
- The legacy [`cryptohopper-android-sdk`](https://github.com/cryptohopper/cryptohopper-android-sdk) targets Android only via JitPack and has known auth-flow bugs ([#99](https://github.com/cryptohopper/cryptohopper-android-sdk/issues/99)).
- This SDK is JVM-target — works on Android *and* server-side Kotlin / Java interop. Sends the correct `access-token` header from day one.
- Modern Kotlin stack: 2.0, coroutines, kotlinx-serialization, OkHttp 4.

### Out of scope (post-alpha)
- Maven Central publishing setup. Currently builds locally via `./gradlew build`; the release workflow + signing key + Sonatype-OSSRH credentials need configuring before the first publish.
- HMAC request signing (`x-api-signature`) — bearer-only for now.
- Mobile device-id flow.
