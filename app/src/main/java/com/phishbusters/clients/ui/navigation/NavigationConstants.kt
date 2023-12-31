package com.phishbusters.clients.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

public sealed class NavDestinations(
    val displayText: String,
    val route: String,
    val icon: @Composable () -> Unit,
    val deepLink: String? = null
) {
    object Main :
        NavDestinations("Inicio", "main", { Icon(Icons.Filled.Home, contentDescription = null) })

    object Settings : NavDestinations(
        "Configuración", "settings",
        { Icon(Icons.Filled.Settings, contentDescription = null) })

    object Stepper : NavDestinations(
        "Stepper", "stepper",
        { Icon(Icons.Filled.Info, contentDescription = null) })

    object Tips : NavDestinations(
        "Tips", "tips",
        { Icon(Icons.Filled.Info, contentDescription = null) })

    object Notifications : NavDestinations(
        "Notificaciones",
        "notifications",
        { Icon(Icons.Filled.Notifications, contentDescription = null) },
        deepLink = "phishbusters://notifications"
    )
}