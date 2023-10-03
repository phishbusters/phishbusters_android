package com.phishbusters.clients.services.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.phishbusters.clients.PhishbustersApp
import com.phishbusters.clients.data.analyze.AnalyzeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProfileReadAccessibilityService : AccessibilityService() {
    private val analyzeRepository: AnalyzeRepository
        get() = (application as PhishbustersApp).container.analyzeRepository
    private var screenNameBuffer = mutableSetOf<String>()
    private val mutex = Mutex()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == "com.twitter.android") {
            val screenName = findScreenName(rootInActiveWindow)
            if (screenName != null && !screenNameBuffer.contains(screenName)) {
//                println("Screen name: $screenName")
                screenNameBuffer.add(screenName)
                CoroutineScope(Dispatchers.IO).launch {
                    mutex.withLock {
                        analyzeRepository.processProfile(screenName)
                        delay(500)  // Tiempo de espera de 500 ms antes de procesar el siguiente
                    }
                }
            }
        } else {
            screenNameBuffer.clear()
        }
    }

    private fun findScreenName(node: AccessibilityNodeInfo?): String? {
        if (node == null) return null

        val textContent = node.text?.toString()
        if (textContent != null && textContent.startsWith("@")) {
            return textContent
        }

        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)
            val screenName = findScreenName(childNode)
            if (screenName != null) return screenName
        }

        return null
    }

    override fun onInterrupt() {
        println("Profile service stopped.")
        val intent = Intent("com.phishbusters.clients.ACCESSIBILITY_STATUS_CHANGED")
        intent.putExtra("service_name", "ProfileAccessibilityService")
        intent.putExtra("status", "DISCONNECTED")
        sendBroadcast(intent)
        screenNameBuffer.clear()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        println("Profile service started.")
        val intent = Intent("com.phishbusters.clients.ACCESSIBILITY_STATUS_CHANGED")
        intent.putExtra("service_name", "ProfileAccessibilityService")
        intent.putExtra("status", "CONNECTED")
        sendBroadcast(intent)
    }
}