package com.phishbusters.clients.ui.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


sealed interface TipsUiState {

    data class Default(var dummy: Boolean) : TipsUiState
}

private data class TipsViewModelState(
    var dummy: Boolean,
) {
    fun toUiState(): TipsUiState =
        TipsUiState.Default(false)
}

class TipsViewModel : ViewModel() {
    private val viewModelState = MutableStateFlow(
        TipsViewModelState(
            false
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(TipsViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {

    }

    /**
     * Factory for HomeViewModel
     */
    companion object {
        fun provideFactory(): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TipsViewModel() as T
            }
        }
    }
}
