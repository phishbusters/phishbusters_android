package com.phishbusters.clients.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.phishbusters.clients.R
import com.phishbusters.clients.ui.navigation.NavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = NavDestinations.Settings.displayText,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    if (!isExpandedScreen) {
                        IconButton(onClick = openDrawer) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "open/close",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Search is not yet implemented in this configuration",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Tests"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)
        settingsContent(
            isExpandedScreen,
            screenModifier
        )

    }
}

@Composable
private fun settingsContent(
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { openAccessibilitySettings(context) }) {
                Text("Activar Servicios")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Para activar los servicios, sigue estos pasos:",
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.height(8.dp))
                listOf(
                    "Busca 'Phishbusters' en la lista.",
                    "Activa los servicios.",
                    "Confirma la activación."
                ).forEach { instruction ->
                    Text(text = instruction, textAlign = TextAlign.Left)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            serviceDescription(
                title = "Servicio de análisis de chats:",
                description = "Analiza los chats en tiempo real para detectar posibles ataques de phishing."
            )
            Spacer(modifier = Modifier.height(8.dp))
            serviceDescription(
                title = "Servicio de análisis de perfiles:",
                description = "Examina los perfiles de los usuarios para identificar comportamientos sospechosos."
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = "Descripción del GIF para accesibilidad",
            )
        }
    }
}

@Composable
private fun serviceDescription(title: String, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp),
            textAlign = TextAlign.Center
        )
    }
}

private fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}
