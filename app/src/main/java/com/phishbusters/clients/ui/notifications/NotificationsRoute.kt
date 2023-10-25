package com.phishbusters.clients.ui.notifications

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun NotificationsRoute(
    notificationsViewModel: NotificationsViewModel,
    navController: NavController,
    snackBarHostState: SnackbarHostState = SnackbarHostState()
) {
    val uiState by notificationsViewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreen(
        uiState = uiState,
        navController = navController,
        snackBarHostState = snackBarHostState
    )
}
