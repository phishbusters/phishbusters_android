package com.phishbusters.clients.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.phishbusters.clients.data.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


sealed interface SettingsUiState {

    val isLoading: Boolean

    data class Default(
        override val isLoading: Boolean,
    ) : SettingsUiState
}

/**
 * An internal representation of the Home route state, in a raw form
 */
private data class SettingsViewModelState(
    val isLoading: Boolean = false,
) {
    fun toUiState(): SettingsUiState =
        SettingsUiState.Default(isLoading = isLoading)
}

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        SettingsViewModelState(
            isLoading = true,
        )
    )

    val uiState = viewModelState
        .map(SettingsViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {

    }

    companion object {
        fun provideFactory(
            settingsRepository: SettingsRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(settingsRepository) as T
            }
        }
    }
}