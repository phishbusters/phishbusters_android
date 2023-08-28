package com.phishbusters.clients.ui.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


sealed interface TutorialUiState {
    val step: Int

    data class Default(
        override val step: Int = 0
    ) : TutorialUiState
}

private data class TutorialViewModelState(
    val step: Int = 0
) {
    fun toUiState(): TutorialUiState =
        TutorialUiState.Default(
            step
        )

}

class TutorialViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(
        TutorialViewModelState(
            step = 1,
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(TutorialViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {

    }

    fun onNextClick() {
        viewModelState.update {
            it.copy(
                step = viewModelState.value.step + 1
            )
        }
    }

    /**
     * Factory for HomeViewModel
     */
    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TutorialViewModel() as T
            }
        }
    }
}
