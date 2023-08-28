package com.phishbusters.clients.ui.tutorial

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phishbusters.clients.R

@Composable
fun TutorialScreen(
    tutorialUiState: TutorialUiState,
    onTutorialCompleted: () -> Unit,
    onNextClick: () -> Unit,
) {
    val currentStep = tutorialUiState.step
    val steps = listOf(
        TutorialStep(
            step = 1,
            imageRes = R.drawable.tutorial1,
            text = "Una solución integral para proteger tu dispositivo contra intentos de phising y estafas en linea.",
            title = "Bienvenido a Phishbusters",
        ), TutorialStep(
            step = 2,
            imageRes = R.drawable.tutorial2,
            text = "Phisbusters utiliza IA para realizar analisis de texto y de patrones de comportamiento. De esta forma evitarmeos en tiempo real las amenzasas en las redes sociales",
            title = "Inteligencia artificial a tu servicio",
        ), TutorialStep(
            step = 3,
            imageRes = R.drawable.tutorial3,
            text = "La informacion analizada nunca saldra de tu dispositivo ni se utilizara con otros fines que no sean la deteccion de amenzas de seguridad.",
            title = "Nos tomamos tu privacidad muy enserio",
            showTermsAndCond = true,
        )
    )
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(
            targetState = currentStep,
            animationSpec = tween(1000),
            label = "animation"
        ) { step ->
            TutorialScreen(
                tutorialStep = steps[step - 1],
                onNext = if (currentStep == 3) onTutorialCompleted else onNextClick,
                currentStep = currentStep
            )
        }
    }
}

data class TutorialStep(
    val step: Int,
    val imageRes: Int,
    val title: String,
    val text: String,
    val showTermsAndCond: Boolean? = null,
)

@Composable
private fun TutorialScreen(
    tutorialStep: TutorialStep,
    onNext: () -> Unit,
    currentStep: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = tutorialStep.imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f, fill = true)
        )
        Text(
            text = tutorialStep.title, textAlign = TextAlign.Center, style = TextStyle(
                fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black
            ), modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Text(
            text = tutorialStep.text, textAlign = TextAlign.Center, style = TextStyle(
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 24.sp,
            ), modifier = Modifier.fillMaxWidth()
        )
        tutorialStep.showTermsAndCond?.let {
            val annotatedText = buildAnnotatedString {
                append("Al continuar, usted acepta nuestros ")
                pushStyle(SpanStyle(textDecoration = TextDecoration.Underline, color = Color(0xFF0d6efd)))
                append("términos y condiciones.")
                pop()
            }
            Text(
                text = annotatedText,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable {  }
            )
        }
        Box(
            modifier = Modifier
                .weight(1f, fill = true)
                .padding(top = 16.dp)
        ) {
            Button(
                onClick = onNext, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    if (currentStep == 3) "Aceptar y continuar" else "Continuar"
                )
            }
        }
    }

}

