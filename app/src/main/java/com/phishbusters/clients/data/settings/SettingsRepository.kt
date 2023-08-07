package com.phishbusters.clients.data.settings

interface SettingsRepository {
    suspend fun verifyMessages(message: String?): Result<String>
}