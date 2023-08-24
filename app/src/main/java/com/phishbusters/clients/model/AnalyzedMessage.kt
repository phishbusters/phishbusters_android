package com.phishbusters.clients.model

data class AnalyzedMessage(
    val prediction: Boolean,
    val trustLevel: String,
)
