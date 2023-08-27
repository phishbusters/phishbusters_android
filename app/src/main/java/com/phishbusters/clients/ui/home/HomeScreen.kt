package com.phishbusters.clients.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phishbusters.clients.model.ClientStatistics
import com.phishbusters.clients.ui.components.AppSnackBarHost
import com.phishbusters.clients.ui.components.AppTopBar
import com.phishbusters.clients.ui.components.BarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    showTopAppBar: Boolean,
    openDrawer: () -> Unit,
    navigateToSettings: () -> Unit,
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
        Column(
            modifier = contentModifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ServiceStatus(
                services = uiState.accessibilityServiceStatus,
                navigateToSettings = navigateToSettings
            )
            Spacer(modifier = Modifier.height(16.dp))
            uiState.statistics?.let { PhishingStats(it) }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ServiceStatus(services: AccessibilityServiceStatus, navigateToSettings: () -> Unit) {
    val serviceStatus = services.getBothServicesStatus()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navigateToSettings() }
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .shadow(elevation = 2.dp, RoundedCornerShape(2.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val textModifier = Modifier.padding(start = 8.dp)
            val iconModifier = Modifier.size(36.dp)
            val textStyle =
                TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
            when (serviceStatus) {
                ServiceStatus.CONNECTED -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Connected",
                        modifier = iconModifier,
                        tint = Color(0xFF006400),
                    )
                    Text(
                        "Todos los servicios están activos. Su seguridad está garantizada.",
                        modifier = textModifier,
                        style = textStyle,
                    )
                }

                ServiceStatus.PARTIAL -> {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Partial",
                        modifier = iconModifier,
                        tint = Color(0xFFDAA520),
                    )
                    Text(
                        "Algunos servicios no están activos. Su protección podría no ser completa.",
                        modifier = textModifier,
                        style = textStyle,
                    )
                }

                ServiceStatus.DISCONNECTED -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Disconnected",
                        modifier = iconModifier,
                        tint = Color.Red,
                    )
                    Text(
                        "Ningún servicio de seguridad está activo. Haga clic para activar y asegurar su dispositivo.",
                        modifier = textModifier,
                        style = textStyle,
                    )
                }
            }
        }
    }
}

@Composable
private fun PhishingStats(
    statistics: ClientStatistics
) {
    val values = listOf(
        statistics.phishingAttempts.toFloat(),
        statistics.successfulBlocks.toFloat()
    )

    BarChart(
        values = values,
        legends = listOf("Phishing Attempts", "Successful Blocks"),
        maxHeight = 200.dp
    )
}
