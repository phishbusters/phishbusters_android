package com.phishbusters.clients.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phishbusters.clients.R
import com.phishbusters.clients.ui.components.AppTopBar
import com.phishbusters.clients.ui.navigation.NavDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppTopBar(openDrawer = openDrawer)
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
                Text("Activar Servicios de Accesibilidad")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Para habilitar los servicios de accesibilidad, siga los siguientes pasos:",
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = ParagraphStyle(lineHeight = 20.sp)) {
                            append("1. Navegue hasta 'Configuración de Accesibilidad' en su dispositivo.\n")
                            append("2. Busque 'Phishbusters' en la lista de servicios disponibles.\n")
                            append("3. Habilite los siguientes servicios y confirme su activación:\n")
                            append("    - Servicio de Detección en Chats\n")
                            append("    - Servicio de Identificación de Perfiles")
                        }
                    },
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ServiceDescription(
                title = "Servicio de análisis de chats:",
                description = "Este servicio escanea sus conversaciones en tiempo real para identificar y prevenir posibles intentos de phishing."
            )
            Spacer(modifier = Modifier.height(8.dp))
            ServiceDescription(
                title = "Servicio de detección de perfiles:",
                description = "Este servicio examina los perfiles de usuario en diversas plataformas para detectar comportamientos sospechosos y potencialmente maliciosos."
            )
        }
    }
}

@Composable
private fun ServiceDescription(title: String, description: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
    }
}

private fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}
