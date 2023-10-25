package com.phishbusters.clients.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phishbusters.clients.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    openNotification: () -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppTopBar(
                openDrawer = openDrawer,
                openNotifications = openNotification,
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
            Text(
                text = "Instrucciones para activar el servicio de accesibilidad",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    "Para habilitar los servicios de accesibilidad, siga las indicaciones:",
                    modifier = Modifier.padding(start = 8.dp),
                    style = TextStyle(fontSize = 18.sp, color = Color.DarkGray),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = ParagraphStyle(lineHeight = 20.sp),
                        ) {
                            append("1. Navegue hasta 'Configuración de Accesibilidad' en su dispositivo.\n")
                            append("2. Busque 'Phishbusters' en la lista de servicios disponibles.\n")
                            append("3. Habilite los siguientes servicios y confirme su activación:\n")
                            append("    - Servicio de Detección en Chats\n")
                            append("    - Servicio de Identificación de Perfiles")
                        }
                    },
                    textAlign = TextAlign.Left,
                    style = TextStyle(fontSize = 14.sp),
                    color = Color.DarkGray,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Información adicional",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Cómo funciona nuestro servicio de accesibilidad",
                    style = TextStyle(fontSize = 16.sp, color = Color.DarkGray),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                    color = Color.DarkGray,
                )
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = ParagraphStyle(lineHeight = 20.sp),
                        ) {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Servicio de Chats: ")
                            }
                            append("Este servicio te protege en tus chats al detectar intentos de estafa en tiempo real.\n")

                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Servicio de Detección de Perfiles: ")
                            }
                            append("Con este servicio, analizamos perfiles en redes sociales y otras plataformas para alertarte sobre posibles cuentas falsas o peligrosas.")
                        }
                    },
                    textAlign = TextAlign.Left,
                    style = TextStyle(fontSize = 14.sp),
                    color = Color.DarkGray,
                )

                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Button(onClick = { openAccessibilitySettings(context) }) {
                        Text("Activar Servicios de Accesibilidad")
                    }
                }
            }
        }
    }
}

private fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    context.startActivity(intent)
}
