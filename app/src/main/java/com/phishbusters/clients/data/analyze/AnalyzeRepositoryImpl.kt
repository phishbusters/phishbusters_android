package com.phishbusters.clients.data.analyze

import com.phishbusters.clients.data.ConfigVars
import com.phishbusters.clients.services.notifications.NotificationService
import com.phishbusters.clients.model.AnalyzedMessage
import com.phishbusters.clients.model.AnalyzedProfile
import com.phishbusters.clients.model.Prediction
import com.phishbusters.clients.model.ProfilePrediction
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
        val newMessages = messages.filterNot { processedMessages.contains(it) }
        if (newMessages.isNotEmpty()) {
            val endpoint = ConfigVars.API_URL + "/chat/analyze"
            processedMessages.addAll(newMessages.reversed())
            val messages = processedMessages.toArray()
            val payload = mapOf(
                "messages" to messages,
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
                        val response = result.data
                        if (response.prediction == Prediction.PHISHING.stringValue) {
                            notificationService.showPhishingAlert(response.confidence)
                        }
                    }

                    is ApiResult.Error -> {
                        // Ignore for now
                        print(result.exception?.message)
                    }
                }
            }
        }
    }

    override suspend fun processProfile(screenName: String) {
        var finalScreenName = screenName
        if (finalScreenName.startsWith("@")) {
            finalScreenName = finalScreenName.substring(1)
        }

        val endpoint = ConfigVars.API_URL + "/profile/analyze"
        val payload = mapOf("screenName" to finalScreenName)
        apiService.httpCallWithPayload<AnalyzedProfile>(
            endpoint,
            HttpMethod.POST,
            payload
        ) { result ->
            when (result) {
                is ApiResult.Success -> {
                    val response = result.data
                    if (response.predictionLabel == ProfilePrediction.FAKE.stringValue) {
                        notificationService.showProfileAlert(screenName, response.confidence)
                    }
                }

                is ApiResult.Error -> {
                    // Ignore for now
                    print(result.exception?.message)
                }
            }
        }
    }

    override fun cleanMessages() {
        processedMessages.clear()
    }
}