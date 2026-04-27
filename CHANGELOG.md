# Changelog

All notable changes to `com.cryptohopper:cryptohopper` are documented here.
The format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and
the project adheres to [Semantic Versioning](https://semver.org/).

## 0.1.0-alpha.1 — Unreleased

Initial alpha release. Scaffold + transport + first resource (`user`). Subsequent alpha releases land the remaining 17 API domains.

### Added

- **`com.cryptohopper.Client`** — async client built on OkHttp with a fluent `Client.Builder`. `Client.create(apiKey)` shortcut for the simple case.
- **`CryptohopperError`** — single exception type whose `code` follows the shared SDK taxonomy (`UNAUTHORIZED`, `FORBIDDEN`, `NOT_FOUND`, `RATE_LIMITED`, `VALIDATION_ERROR`, `DEVICE_UNAUTHORIZED`, `CONFLICT`, `SERVER_ERROR`, `SERVICE_UNAVAILABLE`, `NETWORK_ERROR`, `TIMEOUT`, `UNKNOWN`). Carries `code`, `status`, `serverCode`, `ipAddress`, `retryAfterMs`. Stable across SDKs.
- **`Transport`** — internal HTTP layer. Auto-retries 429 honouring `Retry-After`, auto-unwraps the `{"data": ...}` envelope, maps every error path to `CryptohopperError`. Sends the v1 API's `access-token` header (not the OAuth2-conventional `Authorization: Bearer` — see [why](https://www.cryptohopper.com/api-documentation/how-the-api-works)).
- **`UserResource`** — first resource, available as `client.user.get()`.
- **`x-api-app-key`** support via `Builder.appKey(...)` for per-app rate-limit attribution.
- **Custom OkHttp client injection** via `Builder.httpClient(...)` for proxies, mTLS, instrumentation.
- **Configurable timeout, max-retries, base URL** through the builder.

### Tests
- 5 transport-level tests using OkHttp's `MockWebServer`: access-token header presence, `Authorization` *absence*, app-key forwarding, 429 retry honouring `Retry-After`, 403 with `ip_address`, empty-apiKey rejection at build time.

### Why a Kotlin SDK
- The legacy [`cryptohopper-android-sdk`](https://github.com/cryptohopper/cryptohopper-android-sdk) targets Android only via JitPack and has known auth-flow bugs ([#99](https://github.com/cryptohopper/cryptohopper-android-sdk/issues/99)).
- This SDK is JVM-target — works on Android *and* server-side Kotlin / Java interop. Distributed via Maven Central (once configured). Sends the correct `access-token` header from day one.

### Out of scope (post-alpha)
- The other 17 API domains (`hoppers`, `exchange`, `strategy`, `backtest`, `market`, `signals`, `arbitrage`, `marketmaker`, `template`, `ai`, `platform`, `chart`, `subscription`, `social`, `tournaments`, `webhooks`, `app`). Land in subsequent alphas.
- Maven Central publishing setup. Currently builds locally via `./gradlew build`; the release workflow + signing key + Sonatype-OSSRH credentials need configuring before the first publish.
- HMAC request signing (`x-api-signature`) — bearer-only for now.
- Mobile device-id flow.
