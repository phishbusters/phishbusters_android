package com.phishbusters.clients.services.broadcast

import android.content.BroadcastReceiver
import android.content.IntentFilter

interface BroadcastService {
    fun registerReceiver(accessibilityReceiver: BroadcastReceiver, filter: IntentFilter)

    fun unregisterReceiver(accessibilityReceiver: BroadcastReceiver)

    fun getInitialServiceStatus()
}