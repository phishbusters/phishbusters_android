package com.phishbusters.clients.services.notifications

interface NotificationService {
    fun showPhishingAlert(confidence: Double)
}
