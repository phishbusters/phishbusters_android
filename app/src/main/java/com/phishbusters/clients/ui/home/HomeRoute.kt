package com.phishbusters.clients.ui.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState = SnackbarHostState()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeRoute(
        uiState = uiState,
        isExpandedScreen = isExpandedScreen,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState,
    )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val homeListLazyListState = rememberLazyListState()
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
        uiState = uiState,
        showTopAppBar = !isExpandedScreen,
        openDrawer = openDrawer,
        snackBarHostState = snackBarHostState,
    )
}