# cryptohopper-kotlin-sdk

[![CI](https://github.com/cryptohopper/cryptohopper-kotlin-sdk/actions/workflows/ci.yml/badge.svg)](https://github.com/cryptohopper/cryptohopper-kotlin-sdk/actions/workflows/ci.yml)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0%2B-blue?logo=kotlin)](build.gradle.kts)
[![JVM](https://img.shields.io/badge/jvm-17%2B-blue?logo=openjdk)](build.gradle.kts)
[![License: MIT](https://img.shields.io/github/license/cryptohopper/cryptohopper-kotlin-sdk?color=blue)](LICENSE)

Official Kotlin SDK for the [Cryptohopper](https://www.cryptohopper.com) API. Cross-platform JVM — works on Android *and* server-side Kotlin/Java.

> **Status: 0.1.0-alpha.1 — full coverage of all 18 public API domains** (`user`, `hoppers`, `exchange`, `strategy`, `backtest`, `market`, `signals`, `arbitrage`, `marketmaker`, `template`, `ai`, `platform`, `chart`, `subscription`, `social`, `tournaments`, `webhooks`, `app`). Same surface as the other official SDKs.

## Why this SDK

The legacy [`cryptohopper-android-sdk`](https://github.com/cryptohopper/cryptohopper-android-sdk) targets Android only via JitPack and has [known auth-flow bugs](https://github.com/cryptohopper/cryptohopper-android-sdk/issues/99). This SDK is a fresh start with:

- **Cross-JVM target** — works on Android *and* server-side Kotlin / Java interop.
- **Correct `access-token` header from day one** — the legacy SDK sends `Authorization: Bearer` for v1 calls, which the API gateway rejects (see [the live API docs](https://www.cryptohopper.com/api-documentation/how-the-api-works)).
- **Modern stack** — Kotlin 2.3, coroutines, kotlinx-serialization, OkHttp 5.
- **Same surface as every other official SDK** in the suite — Node, Python, Go, Ruby, Rust, PHP, Dart, Swift. Same error taxonomy. Same retry contract. Same idioms ported per language.

If you're building an Android-only app today, the legacy SDK still works once the open issues are addressed. If you're writing server-side Kotlin or want a fresh foundation, use this one.

## Install

> **Pre-release.** Maven Central publishing isn't configured yet — for now build locally:
>
> ```bash
> git clone https://github.com/cryptohopper/cryptohopper-kotlin-sdk
> cd cryptohopper-kotlin-sdk
> ./gradlew publishToMavenLocal
> ```
>
> Then in your project's `build.gradle.kts`:
>
> ```kotlin
> repositories {
>     mavenLocal()
>     mavenCentral()
> }
> dependencies {
>     implementation("com.cryptohopper:cryptohopper:0.1.0-alpha.1")
> }
> ```
>
> Once the package lands on Maven Central, the `mavenLocal()` line drops out and you depend on the central artifact directly.

## Quickstart

```kotlin
import com.cryptohopper.Client
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val ch = Client.create(System.getenv("CRYPTOHOPPER_TOKEN"))
    val me = ch.user.get()
    println(me)
}
```

Requires Kotlin 2.3+ and JVM 17+.

## Authentication

Cryptohopper uses OAuth2. To get a token:

1. Sign in at [cryptohopper.com](https://www.cryptohopper.com) and open the developer dashboard.
2. **Create an OAuth application** — you receive a `client_id` and `client_secret`.
3. **Drive the OAuth consent flow** to receive a 40-character bearer token scoped to the permissions you requested. This SDK does not handle the consent exchange itself — that's a one-time setup.

Pass the resulting token to the client:

```kotlin
val ch = Client.builder()
    .apiKey(System.getenv("CRYPTOHOPPER_TOKEN"))
    .appKey(System.getenv("CRYPTOHOPPER_APP_KEY"))  // optional, sent as x-api-app-key
    .build()
```

The SDK sends the token in an `access-token: <token>` HTTP header — *not* `Authorization: Bearer <token>`. Cryptohopper's v1 API gateway rejects Bearer-style headers (it routes them into a SigV4 parser). The live API docs at <https://www.cryptohopper.com/api-documentation/how-the-api-works> are authoritative for this.

## Errors

Every non-2xx response and every transport failure throws `CryptohopperError`:

```kotlin
import com.cryptohopper.CryptohopperError

try {
    ch.user.get()
} catch (e: CryptohopperError) {
    println(e.code)          // "UNAUTHORIZED" | "FORBIDDEN" | "RATE_LIMITED" | ...
    println(e.status)        // HTTP status; 0 for transport-level failures
    println(e.serverCode)    // numeric server code, if any
    println(e.ipAddress)     // client IP the server saw (debug IP-allowlist mismatches)
    println(e.retryAfterMs)  // populated on 429
}
```

Codes are **stable across every official Cryptohopper SDK**. Compare with `==`, never substring-match.

## Rate limiting

The server enforces three buckets:

- `normal` — 30 requests/minute
- `order` — 8 orders per 8-second window
- `backtest` — 1 request per 2 seconds

On HTTP 429 the SDK retries with exponential back-off up to `maxRetries` (default 3), respecting `Retry-After`. Pass `maxRetries(0)` to the builder to disable auto-retry and handle 429s yourself.

## Configuration

```kotlin
val ch = Client.builder()
    .apiKey(System.getenv("CRYPTOHOPPER_TOKEN"))
    .appKey(System.getenv("CRYPTOHOPPER_APP_KEY"))
    .baseUrl("https://api.cryptohopper.com/v1")    // override for staging
    .timeout(30, TimeUnit.SECONDS)                  // per-request total deadline
    .maxRetries(3)                                  // 429 backoff; 0 disables
    .userAgent("my-app/1.0")                        // appended to UA
    .httpClient(myCustomOkHttp)                     // BYO OkHttp for proxies, mTLS, etc.
    .build()
```

## Sibling SDKs

| Language | Package | Install |
|---|---|---|
| Node.js / TypeScript | [`@cryptohopper/sdk`](https://www.npmjs.com/package/@cryptohopper/sdk) | `npm i @cryptohopper/sdk` |
| Python | [`cryptohopper`](https://pypi.org/project/cryptohopper/) | `pip install cryptohopper` |
| Go | `github.com/cryptohopper/cryptohopper-go-sdk` | `go get github.com/cryptohopper/cryptohopper-go-sdk` |
| Ruby | [`cryptohopper`](https://rubygems.org/gems/cryptohopper) | `gem install cryptohopper --pre` |
| Rust | [`cryptohopper`](https://crates.io/crates/cryptohopper) | `cargo add cryptohopper` |
| PHP | [`cryptohopper/sdk`](https://packagist.org/packages/cryptohopper/sdk) | `composer require cryptohopper/sdk` |
| Dart | [`cryptohopper`](https://pub.dev/packages/cryptohopper) | `dart pub add cryptohopper` |
| Swift | SwiftPM | Add `https://github.com/cryptohopper/cryptohopper-swift-sdk` |
| **Kotlin** | this repo | (Maven Central publish pending) |
| CLI | [`@cryptohopper/cli`](https://www.npmjs.com/package/@cryptohopper/cli) | `npm i -g @cryptohopper/cli` |

All ten share the same 18 API domains, same error taxonomy, same retry semantics. MIT-licensed.

## Development

```bash
./gradlew build            # compile + test
./gradlew test             # tests only
./gradlew publishToMavenLocal
```

## License

MIT — see [LICENSE](./LICENSE).
