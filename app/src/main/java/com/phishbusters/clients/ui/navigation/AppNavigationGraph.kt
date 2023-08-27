package com.phishbusters.clients.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phishbusters.clients.data.AppContainer
import com.phishbusters.clients.ui.home.HomeRoute
import com.phishbusters.clients.ui.home.HomeViewModel
import com.phishbusters.clients.ui.settings.SettingsRoute
import com.phishbusters.clients.ui.settings.SettingsViewModel

@Composable
fun AppNavigationGraph(
    appContainer: AppContainer,
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = NavDestinations.Main.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = NavDestinations.Main.route) { navBackStackEntry ->
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(
                    homeRepository = appContainer.homeRepository,
                    broadcastService = appContainer.broadcastService
                )
            )

            HomeRoute(
                navController = navController,
                homeViewModel = homeViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer,
            )
        }

        composable(route = NavDestinations.Settings.route) { navBackStackEntry ->
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModel.provideFactory(appContainer.settingsRepository)
            )

            SettingsRoute(
                settingsViewModel = settingsViewModel,
                isExpandedScreen = isExpandedScreen,
                openDrawer = openDrawer
            )
        }
    }
}