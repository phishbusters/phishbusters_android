package com.phishbusters.clients.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
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
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.phishbusters.clients.ui.components.AppLineChart
import com.phishbusters.clients.ui.components.AppLoadingIndicator
import com.phishbusters.clients.ui.components.AppSnackBarHost
import com.phishbusters.clients.ui.components.AppTopBar
import com.phishbusters.clients.ui.components.DashboardIndicatorBox

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ServiceStatus(
                services = uiState.accessibilityServiceStatus,
                navigateToSettings = navigateToSettings
            )
            if (uiState.isLoading) {
                Box(modifier = Modifier.padding(top = 12.dp)) {
                    AppLoadingIndicator()
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                uiState.statistics?.let {
                    val phishingChatsMap = it.entries.mapIndexed { index, entry ->
                        index.toFloat() to entry.value.phishingChatsDetected.toFloat()
                    }.toMap()
                    val fakeProfilesMap = it.entries.mapIndexed { index, entry ->
                        index.toFloat() to entry.value.fakeProfilesDetected.toFloat()
                    }.toMap()
                    val sortedDates = it.keys.sorted()
                    val dateAxisValueFormatter =
                        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
                            sortedDates.getOrNull(x.toInt()) ?: ""
                        }

                    uiState.totalStats.let {
                        Text(
                            text = "Totales detectados",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            color = Color.DarkGray
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            DashboardIndicatorBox(
                                "Cuentas fraudulentas",
                                it?.totalFakeProfiles.toString() ?: "0"
                            )
                            DashboardIndicatorBox(
                                "Intentos de phishing",
                                it?.totalPhishingChats.toString() ?: "0"
                            )
                        }
                    }

                    ChartSection(
                        title = "Intentos de phishing detectados por chat en los últimos 7 días",
                        data = phishingChatsMap,
                        valueFormatter = dateAxisValueFormatter
                    )

                    ChartSection(
                        title = "Bloqueos de perfiles realizados por el sistema en en los últimos 7 días",
                        data = fakeProfilesMap,
                        valueFormatter = dateAxisValueFormatter
                    )
                }
                if (uiState.errorMessage.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(8.dp),
                            )
                            .shadow(elevation = 2.dp, RoundedCornerShape(2.dp))
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = uiState.errorMessage,
                                modifier = Modifier.padding(top = 12.dp),
                                color = Color.DarkGray,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                }
            }
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
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .shadow(elevation = 2.dp, RoundedCornerShape(2.dp))
    ) {
        Column() {
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
                    TextStyle(fontSize = 18.sp, color = Color.DarkGray)
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
                            imageVector = Icons.Default.Warning,
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

            if (serviceStatus == ServiceStatus.PARTIAL || serviceStatus == ServiceStatus.DISCONNECTED) {
                Button(
                    onClick = { navigateToSettings() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Proteger ahora")
                }
            }
        }
    }
}

@Composable
private fun ChartSection(
    title: String,
    data: Map<Float, Float>,
    valueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.DarkGray
        )
        AppLineChart(
            data = data,
            xValueFormatter = valueFormatter
        )
        Text(
            text = "Fecha de actualización: ${java.util.Calendar.getInstance().time}",
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.DarkGray
        )
    }
}
