plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
    `maven-publish`
}

group = "com.cryptohopper"
version = "0.1.0-alpha.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

dependencies {
    // OkHttp — battle-tested HTTP client with built-in connection pooling and
    // a small footprint. Same library the legacy Android SDK uses, so users
    // migrating between SDKs see no transport-level surprises.
    api("com.squareup.okhttp3:okhttp:4.12.0")

    // kotlinx-serialization for typed JSON without reflection at runtime.
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    // Kotlin coroutines: every API method is a `suspend fun`.
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        // Strict-mode checks. -Werror would be too aggressive for an alpha
        // (third-party deps trigger warnings); leave warnings visible but
        // not fatal until 1.0.
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = "cryptohopper"
            version = project.version.toString()

            pom {
                name.set("Cryptohopper Kotlin SDK")
                description.set("Official Kotlin SDK for the Cryptohopper API.")
                url.set("https://www.cryptohopper.com")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("cryptohopper")
                        name.set("Cryptohopper")
                    }
                }
                scm {
                    url.set("https://github.com/cryptohopper/cryptohopper-kotlin-sdk")
                }
            }
        }
    }
}
