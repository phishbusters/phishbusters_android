package com.phishbusters.clients.data.analyze

import com.phishbusters.clients.data.ConfigVars
import com.phishbusters.clients.data.notifications.NotificationService
import com.phishbusters.clients.model.AnalyzedMessage
import com.phishbusters.clients.network.ApiResult
import com.phishbusters.clients.network.ApiService
import com.phishbusters.clients.network.HttpMethod

class AnalyzeRepositoryImpl(
    private val apiService: ApiService,
    private val notificationService: NotificationService
) : AnalyzeRepository {
    private val processedMessages = HashSet<String>()

    override suspend fun processMessages(
        messages: List<String>,
        profile: String?,
        profileName: String
    ) {
        print("Receiving messages to process: $messages")
        val newMessages = messages.filterNot { processedMessages.contains(it) }
        if (newMessages.isNotEmpty()) {
            val endpoint = ConfigVars.API_URL + "/analyze"
            processedMessages.addAll(newMessages)
            val messagesString = processedMessages.joinToString(separator = " ")
            val payload = mapOf(
                "messages" to messagesString,
                "profile" to profile,
                "username" to profileName
            )

            apiService.httpCallWithPayload<AnalyzedMessage>(
                endpoint,
                HttpMethod.POST,
                payload
            ) { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val response = result.data as AnalyzedMessage
                        if (response.prediction) {
                            notificationService.showPhishingAlert(response.trustLevel)
                        }
                    }

                    is ApiResult.Error -> {
                        // Maneja el error aquí si es necesario
                    }
                }
            }
        }
    }
}