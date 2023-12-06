package com.phishbusters.clients.services.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.phishbusters.clients.MainActivity
import com.phishbusters.clients.R
import com.phishbusters.clients.data.notification.NotificationRepository
import com.phishbusters.clients.model.NotificationsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class NotificationServiceImpl(
    private val context: Context,
    private val notificationRepository: NotificationRepository,
) : NotificationService {
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

    private fun addFeedbackActionButtons(builder: NotificationCompat.Builder, notificationId: Int) {
        val thumbsUpIntent = Intent("THUMBS_UP")
        thumbsUpIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        thumbsUpIntent.putExtra("notificationId", notificationId)
        val thumbsUpPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            thumbsUpIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.addAction(0, "Correcto👍", thumbsUpPendingIntent)

        val thumbsDownIntent = Intent("THUMBS_DOWN")
        thumbsDownIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        thumbsDownIntent.putExtra("notificationId", notificationId)
        val thumbsDownPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            thumbsDownIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.addAction(0, "Incorrecto 👎", thumbsDownPendingIntent)
    }

    override fun showPhishingAlert(confidence: Double, profile: String) {
        val uri = Uri.parse("phishbusters://notifications")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationId = UUID.randomUUID().hashCode()
        val builder = NotificationCompat.Builder(context, "PHISHING_ALERT_CHANNEL")
            .setSmallIcon(R.drawable.phishbusters_icon)
            .setContentTitle("Alerta de Phishing")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Se ha detectado un posible intento de phishing en tu chat.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        addFeedbackActionButtons(builder, notificationId)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }

        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.addNotification(NotificationsType.PhishingChat, profile)
        }
    }

    override fun showProfileAlert(screenName: String, confidence: Double) {
        val uri = Uri.parse("phishbusters://notifications")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationId = UUID.randomUUID().hashCode()
        val builder = NotificationCompat.Builder(context, "PHISHING_ALERT_CHANNEL")
            .setSmallIcon(R.drawable.phishbusters_icon)
            .setContentTitle("Alerta de perfil falso")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Se ha detectado que el perfil $screenName podría ser falso.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        addFeedbackActionButtons(builder, notificationId)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(notificationId, builder.build())
        }

        CoroutineScope(Dispatchers.IO).launch {
            notificationRepository.addNotification(NotificationsType.FakeProfile, screenName)
        }
    }
}