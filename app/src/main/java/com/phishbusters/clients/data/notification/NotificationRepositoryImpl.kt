package com.phishbusters.clients.data.notification

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.phishbusters.clients.R
import com.phishbusters.clients.model.NotificationData
import com.phishbusters.clients.model.NotificationsType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationRepositoryImpl(private val context: Context) : NotificationRepository {
    private val sharedPreferences =
        context.getSharedPreferences("notifications", Context.MODE_PRIVATE)
    private val _notificationsFlow = MutableStateFlow<List<NotificationData>>(emptyList())
    override val notificationsFlow: StateFlow<List<NotificationData>> = _notificationsFlow

    init {
        _notificationsFlow.value = getNotificationFromPrefs()
    }

    override suspend fun addNotification(type: NotificationsType, profile: String) {
        val usableProfile = profile.ifEmpty { "Desconocido" }
        val (icon, title, message) = when (type) {
            NotificationsType.FakeProfile -> Triple(
                R.drawable.fake_profile_icon,
                context.getString(R.string.fake_profile_detected),
                String.format(context.getString(R.string.fake_profile_detected_message), usableProfile)
            )

            NotificationsType.PhishingChat -> Triple(
                R.drawable.notification_icon,
                context.getString(R.string.phishing_chat_detected),
                String.format(context.getString(R.string.phishing_chat_detected_message), usableProfile)
            )
        }

        val notificationData = NotificationData(icon, title, message)
        val currentNotifications = getNotificationFromPrefs().toMutableList()
        currentNotifications.add(notificationData)
        saveNotificationsToPrefs(currentNotifications)
        _notificationsFlow.value = getNotificationFromPrefs()
        Log.d("NotificationRepository", "Notification added: $notificationsFlow")
    }

    private fun getNotificationFromPrefs(): List<NotificationData> {
        val json = sharedPreferences.getString("notifications", null)
        val type = object : TypeToken<List<NotificationData>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    private fun saveNotificationsToPrefs(notifications: List<NotificationData>) {
        val json = Gson().toJson(notifications)
        sharedPreferences.edit().putString("notifications", json).apply()
    }
}