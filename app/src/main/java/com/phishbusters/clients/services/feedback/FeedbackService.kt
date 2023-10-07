package com.phishbusters.clients.services.feedback

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.phishbusters.clients.PhishbustersApp
import com.phishbusters.clients.data.AppContainer

class FeedbackService : Service() {
    private lateinit var appContainer: AppContainer

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        appContainer = (application as PhishbustersApp).container
        val notificationButtonReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    "THUMBS_UP" -> {
                        Log.d("FeedbackService", "Thumbs up")
                    }

                    "THUMBS_DOWN" -> {
                        Log.d("FeedbackService", "Thumbs down")
                    }
                }

                val notificationId = intent.getIntExtra("notificationId", -1)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.cancel(notificationId)
            }
        }

        appContainer.broadcastService.registerReceiver(
            notificationButtonReceiver,
            IntentFilter().apply {
                addAction("THUMBS_UP")
                addAction("THUMBS_DOWN")
            })
    }
}