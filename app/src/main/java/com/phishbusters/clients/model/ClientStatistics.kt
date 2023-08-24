package com.phishbusters.clients.model

data class ClientStatistics(
    val phishingAttempts: Int,
    val successfulBlocks: Int,
    val lastAttemptDate: String
)