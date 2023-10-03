package com.phishbusters.clients.services.notifications

interface NotificationService {
    fun showPhishingAlert(confidence: Double)

    fun showProfileAlert(screenName: String, confidence: Double)
}
