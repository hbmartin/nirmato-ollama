package org.nirmato.ollama.client

import org.nirmato.ollama.api.ChatApi
import org.nirmato.ollama.api.CompletionsApi
import org.nirmato.ollama.api.EmbeddingsApi
import org.nirmato.ollama.api.ModelsApi
import org.nirmato.ollama.api.OllamaApi
import org.nirmato.ollama.internal.HttpRequester

/**
 * Implementation of [OllamaApi].
 *
 * @param requester http transport layer
 */
internal class OllamaClient internal constructor(private val requester: HttpRequester) : OllamaApi,
    CompletionsApi by CompletionsClient(requester),
    ModelsApi by ModelsClient(requester),
    ChatApi by ChatClient(requester),
    EmbeddingsApi by EmbeddedClient(requester)