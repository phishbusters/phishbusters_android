package com.phishbusters.clients.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.phishbusters.clients.data.home.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface HomeUiState {
    val isLoading: Boolean

    data class Default(
        override val isLoading: Boolean
    ) : HomeUiState
}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
) {
    fun toUiState(): HomeUiState =
        HomeUiState.Default(
            isLoading = isLoading,
        )
}

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = true,
        )
    )

    // UI state exposed to the UI
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        // Things to do when start
    }

    /**
     * Factory for HomeViewModel
     */
    companion object {
        fun provideFactory(
            homeRepository: HomeRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(homeRepository) as T
            }
        }
    }
}