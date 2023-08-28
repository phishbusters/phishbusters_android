package com.phishbusters.clients.ui.tutorial

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TutorialRoute(
    tutorialViewModel: TutorialViewModel,
    onTutorialCompleted: () -> Unit
) {
    val uiState by tutorialViewModel.uiState.collectAsStateWithLifecycle()
    TutorialScreen(
        tutorialUiState = uiState,
        onTutorialCompleted = {
            onTutorialCompleted()
        },
        onNextClick = {
            tutorialViewModel.onNextClick()
        }
    )
}
