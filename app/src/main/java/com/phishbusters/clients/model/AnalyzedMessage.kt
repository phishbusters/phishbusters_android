package com.phishbusters.clients.model

data class AnalyzedMessage(
    val prediction: String,
    val confidence: Double,
)

enum class Prediction(val stringValue: String) {
    PHISHING("phishing"),
    NOT_PHISHING("no phishing")
}
