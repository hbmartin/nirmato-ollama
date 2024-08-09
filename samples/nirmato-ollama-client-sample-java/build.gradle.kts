import org.jetbrains.kotlin.config.ApiVersion
import org.jetbrains.kotlin.config.LanguageVersion
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")

    id("build-project-default")
}

kotlin {
    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass.set("org.nirmato.ollama.client.samples.ApplicationKt")
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                apiVersion = ApiVersion.KOTLIN_1_7.toString()
                languageVersion = LanguageVersion.KOTLIN_2_0.toString()
                progressiveMode = true
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":nirmato-ollama-client"))

                implementation(libraries.ktor.client.java)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libraries.kotlinx.coroutines.slf4j)
                implementation(libraries.kotlinx.serialization.json.jvm)
                implementation(libraries.ktor.client.content.negotiation.jvm)
                implementation(libraries.ktor.client.java.jvm)
                implementation(libraries.ktor.client.logging.jvm)
                implementation(libraries.ktor.serialization.kotlinx.json.jvm)
            }
        }
    }
}