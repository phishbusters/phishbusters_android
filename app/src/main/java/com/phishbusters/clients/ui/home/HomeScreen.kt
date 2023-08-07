package com.phishbusters.clients.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.phishbusters.clients.R
import com.phishbusters.clients.ui.components.AppSnackBarHost
import com.phishbusters.clients.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    Scaffold(
        snackbarHost = { AppSnackBarHost(hostState = snackBarHostState) },
        topBar = {
            if (showTopAppBar) {
                AppTopBar(
                    openDrawer = openDrawer,
                    topAppBarState = topAppBarState
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)

        TextButton(
            onClick = {},
            modifier = contentModifier.fillMaxSize()
        ) {
            Text(
                stringResource(id = R.string.app_name),
                textAlign = TextAlign.Center
            )
        }
    }


}
