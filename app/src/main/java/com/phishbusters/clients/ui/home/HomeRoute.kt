package com.phishbusters.clients.ui.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.phishbusters.clients.ui.navigation.NavDestinations

@Composable
fun HomeRoute(
    navController: NavController,
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    openNotification: () -> Unit,
    snackBarHostState: SnackbarHostState = SnackbarHostState()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        isExpandedScreen = isExpandedScreen,
        openDrawer = openDrawer,
        navigateToSettings = { navController.navigate(NavDestinations.Settings.route) },
        snackBarHostState = snackBarHostState,
        openNotification = openNotification,
    )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    openNotification: () -> Unit,
    navigateToSettings: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
//    val homeListLazyListState = rememberLazyListState()
//    val articleDetailLazyListStates = when (uiState) {
//        is HomeUiState.HasPosts -> uiState.postsFeed.allPosts
//        is HomeUiState.NoPosts -> emptyList()
//    }.associate { post ->
//        key(post.id) {
//            post.id to rememberLazyListState()
//        }
//    }

//    val homeScreenType = getHomeScreenType(isExpandedScreen, uiState)

    HomeScreen(
        navigateToSettings = navigateToSettings,
        uiState = uiState,
        showTopAppBar = !isExpandedScreen,
        openDrawer = openDrawer,
        openNotification = openNotification,
        snackBarHostState = snackBarHostState,
    )
}