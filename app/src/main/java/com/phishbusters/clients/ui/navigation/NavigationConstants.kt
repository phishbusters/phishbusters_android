package com.phishbusters.clients.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

public sealed class NavDestinations(
    val displayText: String,
    val route: String,
    val icon: @Composable () -> Unit
) {
    object Main :
        NavDestinations("Inicio", "main", { Icon(Icons.Filled.Home, contentDescription = null) })

    object Settings : NavDestinations(
        "Configuración", "settings",
        { Icon(Icons.Filled.Settings, contentDescription = null) })
}