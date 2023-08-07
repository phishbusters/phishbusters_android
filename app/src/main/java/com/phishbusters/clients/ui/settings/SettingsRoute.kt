package com.phishbusters.clients.ui.settings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun SettingsRoute(
    settingsViewModel: SettingsViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
//    val tabContent = rememberTabContent(interestsViewModel)
//    val (currentSection, updateSection) = rememberSaveable {
//        mutableStateOf(tabContent.first().section)
//    }

    SettingsScreen(
        isExpandedScreen = isExpandedScreen,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState
    )
}