package com.phishbusters.clients.model

data class AnalyzedProfile(
    val prediction: Double,
    val confidence: Double,
    val predictionLabel: String
)

enum class ProfilePrediction(val stringValue: String) {
    FAKE("fake"),
    REAL("real")
}