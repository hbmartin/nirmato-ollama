package org.nirmato.ollama.client

import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import org.nirmato.ollama.api.ChatCompletionRequest.Companion.chatCompletionRequest
import org.nirmato.ollama.api.Message
import org.nirmato.ollama.api.Role

internal class ChatClientTest {

    @Test
    fun generateChatCompletion_validRequest_returnSuccess() = runTest(timeout = 1.minutes) {
        val mockEngine = MockEngine.create {
            addHandler {
                respond(
                    content = """{
                          "model": "llama3",
                          "created_at": "2023-08-04T19:22:45.499127Z",
                          "done": true,
                          "total_duration": 4883583458,
                          "load_duration": 1334875,
                          "prompt_eval_count": 26,
                          "prompt_eval_duration": 342546000,
                          "eval_count": 282,
                          "eval_duration": 4535599000
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

        val generateCompletionRequest = chatCompletionRequest {
            model = "tinyllama"
            messages = listOf(Message(role = Role.USER, content = "Why is the sky blue?"))
        }
        val response = ollamaClient.chatCompletion(generateCompletionRequest)

        println(response.toString())
    }
}
