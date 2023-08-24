package com.phishbusters.clients.data.analyze

import com.phishbusters.clients.model.AnalyzedMessage
import com.phishbusters.clients.network.ApiResult

interface AnalyzeRepository {
    suspend fun processMessages(
        messages: List<String>,
        profile: String?, profileName: String
    )
}