package com.phishbusters.clients.ui.tips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phishbusters.clients.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(
    tipsUiState: TipsUiState,
    openDrawer: () -> Unit,
    openNotification: () -> Unit,
    snackBarHostState: SnackbarHostState,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = "Phishing es un tipo de ataque de ingeniería social en el que se engaña a las personas para que revelen información personal o financiera, generalmente a través de un correo electrónico, mensaje o sitio web fraudulento.",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Text(
                    text = "El phishing tiene varias manifestaciones, cada una con características distintivas y métodos de ataque específicos. Los siguientes son algunos de los tipos más prevalentes:",
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.DarkGray
                )
            }

            val phishingTypes = listOf(
                "Phishing por correo electrónico" to "Esta es la forma más común de phishing y ocurre cuando los atacantes envían correos electrónicos fraudulentos que parecen ser de organizaciones legítimas. Estos correos electrónicos a menudo solicitan al destinatario que proporcione información personal o financiera.",
                "Spear phishing" to "Este es un tipo de phishing dirigido en el que los atacantes se centran en un individuo o en una organización específica. Los correos electrónicos de spear phishing son a menudo muy personalizados y pueden parecer que provienen de una fuente de confianza dentro de la organización o grupo."
                // Agregar más tipos aquí
            )

            items(phishingTypes) { (title, description) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = TextStyle(fontSize = 16.sp),
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
