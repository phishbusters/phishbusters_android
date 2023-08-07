package com.phishbusters.clients.ui.navigation.actions

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import com.phishbusters.clients.ui.navigation.NavDestinations

class NavigationActions(navController: NavController) {
    private fun afterNavigate(builder: NavOptionsBuilder, graphStartId: Int) {
        builder.popUpTo(graphStartId) {
            saveState = true
        }

        builder.launchSingleTop = true
        builder.restoreState = true
    }

    val navigateToHome: () -> Unit = {
        navController.navigate(NavDestinations.Main.route) {
            afterNavigate(this, navController.graph.findStartDestination().id)
        }
    }

    val navigateToSettings: () -> Unit = {
        navController.navigate(NavDestinations.Settings.route) {
            afterNavigate(this, navController.graph.findStartDestination().id)
        }
    }
}