package com.phishbusters.clients

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.phishbusters.clients.ui.PhishbustersMain

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_CODE_NOTIFICATION = 1001
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val appContainer = (application as PhishbustersApp).container
        // Solicitar el permiso para publicar notificaciones si es necesario
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_NOTIFICATION
            )
        }

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            PhishbustersMain(
                appContainer, widthSizeClass
            )
        }
    }
}