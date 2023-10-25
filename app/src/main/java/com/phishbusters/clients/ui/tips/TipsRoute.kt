package com.phishbusters.clients.ui.tips

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TipsRoute(
    tipsViewModel: TipsViewModel,
    openDrawer: () -> Unit,
    openNotification: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by tipsViewModel.uiState.collectAsStateWithLifecycle()
    TipsScreen(
        tipsUiState = uiState,
        openDrawer,
        openNotification,
        snackBarHostState,
    )
}
