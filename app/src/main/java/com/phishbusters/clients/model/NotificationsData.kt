package com.phishbusters.clients.model

import org.json.JSONObject

enum class NotificationsType {
    FakeProfile,
    PhishingChat
}

data class NotificationData(
    val icon: Int,
    val title: String,
    val message: String
) {
    fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("icon", icon)
        jsonObject.put("title", title)
        jsonObject.put("message", message)
        return jsonObject.toString()
    }

    companion object {
        fun fromJson(json: String): NotificationData {
            val jsonObject = JSONObject(json)
            return NotificationData(
                jsonObject.getInt("icon"),
                jsonObject.getString("title"),
                jsonObject.getString("message")
            )
        }
    }
}