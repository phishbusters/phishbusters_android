package com.phishbusters.clients.data.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.phishbusters.clients.MainActivity
import com.phishbusters.clients.R

class NotificationServiceImpl(private val context: Context) : NotificationService {
    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alertas de phishing"
            val descriptionText = "Hemos detectado un posible caso de phishing!"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("PHISHING_ALERT_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showPhishingAlert(trustLevel: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, "PHISHING_ALERT_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_background) // Reemplaza esto con tu ícono
            .setContentTitle("Alerta de Phishing")
            .setContentText("Se ha detectado un posible intento de phishing en tu chat.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(0, builder.build())
        }
    }
}