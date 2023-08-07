package com.phishbusters.clients.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ChatAccessibilityService : AccessibilityService() {
    companion object {
        val IGNORE_WORDS = listOf("You", "Tu")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == "com.twitter.android") {
            val messages = findMessages(rootInActiveWindow)
            println("Messages: $messages")
        }
    }

    private fun findMessages(node: AccessibilityNodeInfo): List<String> {
        val messages = mutableListOf<String>()

        val contentDescription = node.contentDescription?.toString()
        val className = node.className.toString()
        if (contentDescription != null && contentDescription.contains(":") &&
            (className == "android.widget.TextView" || className == "android.view.View")
        ) {
            if (!IGNORE_WORDS.any { contentDescription.contains(it, ignoreCase = true) }) {
                messages.add(contentDescription)
            }
        }

        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)
            if (childNode != null) {
                messages.addAll(findMessages(childNode))
            }
        }

        return messages
    }

    override fun onInterrupt() {
        Log.d("ChatAccessibilityService", "Servicio de accesibilidad detenido.")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("ChatAccessibilityService", "Servicio de accesibilidad corriendo.")
    }
}