package com.phishbusters.clients.data.notifications

interface NotificationService {
    fun showPhishingAlert(trustLevel: String)
}
