package com.phishbusters.clients.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.phishbusters.clients.data.home.HomeRepository
import com.phishbusters.clients.model.ClientStatistics
import com.phishbusters.clients.model.Company
import com.phishbusters.clients.network.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    val isLoading: Boolean
    val companies: List<Company>
    val errorMessage: String
    val statistics: ClientStatistics?

    data class Default(
        override val isLoading: Boolean,
        override val companies: List<Company>,
        override val errorMessage: String,
        override val statistics: ClientStatistics? = null
    ) : HomeUiState

//    data class WithStatistics(
//        val statistics: ClientStatistics,
//        override val isLoading: Boolean,
//        override val companies: List<Company>,
//        override val errorMessage: String
//    ) : HomeUiState

}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val companies: List<Company> = listOf(),
    val errorMessage: String = "",
    val statistics: ClientStatistics? = null,
) {
    fun toUiState(): HomeUiState =
        HomeUiState.Default(
            isLoading = isLoading,
            companies = companies,
            errorMessage = errorMessage,
            statistics = statistics,
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
        getCompanies()
        getUsersStatistics()
    }

    private fun getCompanies() {
        viewModelState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = homeRepository.getCompanies()) {
                is ApiResult.Success -> {
                    viewModelState.update { it.copy(isLoading = false, companies = result.data) }
                }

                is ApiResult.Error -> {
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Error al obtener las compañías"
                        )
                    }
                }
            }
        }
    }

    private fun getUsersStatistics() {
        viewModelState.update {
            it.copy(
                isLoading = false,
                statistics = ClientStatistics(15, 33, "")
            )
        }
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