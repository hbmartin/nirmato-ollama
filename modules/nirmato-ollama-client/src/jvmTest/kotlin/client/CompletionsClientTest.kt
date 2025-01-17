package org.nirmato.ollama.client

import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import org.nirmato.ollama.api.CompletionRequest.Companion.completionRequest
import org.nirmato.ollama.api.CompletionResponse

internal class CompletionsClientTest {

    @Test
    fun generateCompletion_validRequest_returnSuccess() = runTest(timeout = 1.minutes) {
        val mockEngine = MockEngine.create {
            addHandler {
                respond(
                    content = """{
                      "model": "llama3",
                      "created_at": "2023-08-04T19:22:45.499127Z",
                      "response": "The sky is blue because it is the color of the sky.",
                      "done": true,
                      "context": [1, 2, 3],
                      "total_duration": 5043500667,
                      "load_duration": 5025959,
                      "prompt_eval_count": 26,
                      "prompt_eval_duration": 325953000,
                      "eval_count": 290,
                      "eval_duration": 4709213000
                    }""",
                    status = HttpStatusCode.OK,
                    headers {
                        append(HttpHeaders.ContentType, "application/json")
                    }
                )
            }
        }

        val ollamaClient = OllamaClient {
            logging = LoggingConfig(logLevel = LogLevel.All)
            timeout = TimeoutConfig(socket = 30.seconds)
            host = OllamaHost.Local
            retry = RetryStrategy(0)
            engine = mockEngine
        }

        val completionRequest = completionRequest {
            model = "tinyllama"
            prompt = "Why is the sky blue?"
        }
        val response = ollamaClient.completion(completionRequest)

        println(response.toString())
    }

    @Test
    fun generateCompletion_validStreamRequest_returnSuccess() = runTest(timeout = 1.minutes) {
        val mockEngine = MockEngine.create {
            addHandler {
                respond(
                    content = """{
                      "model": "llama3",
                      "created_at": "2023-08-04T19:22:45.499127Z",
                      "response": "The sky is blue because it is the color of the sky.",
                      "done": true,
                      "context": [1, 2, 3],
                      "total_duration": 5043500667,
                      "load_duration": 5025959,
                      "prompt_eval_count": 26,
                      "prompt_eval_duration": 325953000,
                      "eval_count": 290,
                      "eval_duration": 4709213000
                    }""",
                    status = HttpStatusCode.OK,
                    headers {
                        append(HttpHeaders.ContentType, "application/json")
                    }
                )
            }
        }

        val ollamaClient = OllamaClient {
            logging = LoggingConfig(logLevel = LogLevel.All)
            timeout = TimeoutConfig(socket = 30.seconds)
            host = OllamaHost.Local
            retry = RetryStrategy(0)
            engine = mockEngine
        }

        val completionRequest = completionRequest {
            model = "tinyllama"
            prompt = "Why is the sky blue?"
        }

        val response = mutableListOf<CompletionResponse>()
        ollamaClient.completionFlow(completionRequest).onEach { response += it }.launchIn(this).join()

        println(response.toString())
    }
}
