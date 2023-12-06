package com.phishbusters.clients.data.notification

import com.phishbusters.clients.model.NotificationData
import com.phishbusters.clients.model.NotificationsType
import kotlinx.coroutines.flow.StateFlow

interface NotificationRepository {
    val notificationsFlow: StateFlow<List<NotificationData>>

    suspend fun addNotification(type: NotificationsType, profileName: String)


}