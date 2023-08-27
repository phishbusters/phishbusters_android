package com.phishbusters.clients.ui.navigation.actions

import androidx.navigation.NavHostController
import com.phishbusters.clients.ui.navigation.NavDestinations

class NavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(NavDestinations.Main.route)
    }

    val navigateToSettings: () -> Unit = {
        navController.navigate(NavDestinations.Settings.route)
    }
}