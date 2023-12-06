package com.phishbusters.clients.services.notifications

interface NotificationService {
    fun showPhishingAlert(confidence: Double, profile: String)

    fun showProfileAlert(screenName: String, confidence: Double)
}
