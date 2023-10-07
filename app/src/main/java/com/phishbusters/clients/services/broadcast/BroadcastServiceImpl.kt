package com.phishbusters.clients.services.broadcast

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.accessibility.AccessibilityManager

class BroadcastServiceImpl(private val context: Context) : BroadcastService {
    override fun registerReceiver(broadcastReceiver: BroadcastReceiver, filter: IntentFilter) {
        context.registerReceiver(broadcastReceiver, filter)
    }

    override fun unregisterReceiver(accessibilityReceiver: BroadcastReceiver) {
        context.unregisterReceiver(accessibilityReceiver)
    }

    override fun getInitialServiceStatus() {
        val customNames = mapOf(
            "com.phishbusters.clients.services.accessibility.ChatAccessibilityService" to "ChatAccessibilityService",
            "com.phishbusters.clients.services.accessibility.ProfileReadAccessibilityService" to "ProfileAccessibilityService"
        )

        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val accessibilityServiceInfo =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        accessibilityServiceInfo.forEach {
            val intent = Intent("com.phishbusters.clients.ACCESSIBILITY_CONNECTED")
            val serviceName = it.resolveInfo.serviceInfo.name
            if (customNames.containsKey(serviceName)) {
                val customName = customNames[serviceName]
                intent.putExtra("service_name", customName)
                intent.putExtra("status", "CONNECTED")
                context.sendBroadcast(intent)
            }
        }
    }
}