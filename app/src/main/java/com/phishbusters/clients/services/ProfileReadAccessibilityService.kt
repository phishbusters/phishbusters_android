package com.phishbusters.clients.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ProfileReadAccessibilityService : AccessibilityService() {
    companion object {
        val READABLE_SCREENS = listOf(297, 298)
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == "com.twitter.android") {
            val username = findProfileName(rootInActiveWindow)
            println("Username: $username")
        }
    }

    private fun findProfileName(node: AccessibilityNodeInfo): String? {
        var username: String? = null

        // Comprueba si este nodo es el nombre de usuario
        val className = node.className.toString()
        if (className == "android.widget.TextView") {
            val text = node.text?.toString()
            if (text != null) {
                // Si el texto comienza con @, es el nombre de usuario
                if (text.startsWith("@")) {
                    return text
                } else if (username == null) {
                    // Si no se ha encontrado un nombre de usuario todavía, guarda este
                    username = text
                }
            }
        }

        // Recorre todos los nodos hijos
        for (i in 0 until node.childCount) {
            val childNode = node.getChild(i)
            if (childNode != null) {
                val childUsername = findProfileName(childNode)
                if (childUsername != null) {
                    // Si el nombre de usuario del hijo comienza con @, devuélvelo
                    if (childUsername.startsWith("@")) {
                        return childUsername
                    } else if (username == null) {
                        // Si no se ha encontrado un nombre de usuario todavía, guarda este
                        username = childUsername
                    }
                }
            }
        }

        // Devuelve el nombre de usuario encontrado, o null si no se encontró ninguno
        return username
    }

//    private fun findProfileName(node: AccessibilityNodeInfo): String? {
//        val className = node.className.toString()
//        if (className == "android.widget.TextView") {
//            return node.text?.toString()
//        }
//
//        for (i in 0 until node.childCount) {
//            val childNode = node.getChild(i)
//            if (childNode != null) {
//                val username = findProfileName(childNode)
//                if (username != null) {
//                    return username
//                }
//            }
//        }
//
//        return null
//    }

    override fun onInterrupt() {
        println("Profile service stopped.")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        println("Profile service started.")
    }
}