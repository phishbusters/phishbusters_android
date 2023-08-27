package com.phishbusters.clients.services.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.phishbusters.clients.PhishbustersApp
import com.phishbusters.clients.data.analyze.AnalyzeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatAccessibilityService : AccessibilityService() {
    private val analyzeRepository: AnalyzeRepository
        get() = (application as PhishbustersApp).container.analyzeRepository
    private var currentWindowId: Int? = null

    companion object {
        val IGNORE_WORDS = listOf("You", "Tu")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == "com.twitter.android") {
            if (event.windowId != currentWindowId) {
                currentWindowId = event.windowId
                analyzeRepository.cleanMessages()
            }

            if (rootInActiveWindow == null) {
                return
            }

            val profile = findProfile(rootInActiveWindow)
            val rawMessages = findMessages(rootInActiveWindow)
            var profileName = ""
            val messages = rawMessages.map {
                profileName = if (it.contains(":")) it.substringBefore(":") else profileName
                it.substringAfter(": ")
                    .replace(Regex("\\s\\d{2}/\\d{2}/\\d{2},\\s\\d{1,2}:\\d{2}\\s[APM]{2}."), "")
                    .trim()
            }
            println("Profile: $profile")
            println("Messages: $messages")

            CoroutineScope(Dispatchers.IO).launch {
                analyzeRepository.processMessages(messages, profile, profileName)
            }
        }
    }

    private fun findProfile(node: AccessibilityNodeInfo): String? {
        val contentDescription = node.contentDescription?.toString()
        val textContent = node.text?.toString()

        if (contentDescription != null && contentDescription.contains("@")) {
            return contentDescription.substringAfter("@").substringBefore(" ")
        }

        if (textContent != null && textContent.contains("@")) {
            return textContent.substringAfter("@").substringBefore(" ")
        }

        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)
            if (childNode != null) {
                val profile = findProfile(childNode)
                if (profile != null) return profile
            }
        }

        return null
    }

    private fun findMessages(node: AccessibilityNodeInfo): List<String> {
        val messages = mutableListOf<String>()

        val contentDescription = node.contentDescription?.toString()
        val className = node.className.toString()
        if (contentDescription != null && contentDescription.contains(":") && (className == "android.widget.TextView" || className == "android.view.View")) {
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
        val intent = Intent("com.phishbusters.clients.ACCESSIBILITY_DISCONNECTED")
        intent.putExtra("service_name", "ChatAccessibilityService")
        intent.putExtra("status", "DISCONNECTED")
        sendBroadcast(intent)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("ChatAccessibilityService", "Servicio de accesibilidad corriendo.")
        val intent = Intent("com.phishbusters.clients.ACCESSIBILITY_CONNECTED")
        intent.putExtra("service_name", "ChatAccessibilityService")
        intent.putExtra("status", "CONNECTED")
        sendBroadcast(intent)
    }
}