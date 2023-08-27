package com.phishbusters.clients.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.phishbusters.clients.data.home.HomeRepository
import com.phishbusters.clients.services.broadcast.BroadcastService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ServiceStatus {
    CONNECTED,
    DISCONNECTED,
    PARTIAL
}

data class PhishingStatsSummary(
    val phishingChatsDetected: Int,
    val fakeProfilesDetected: Int
)

data class AccessibilityServiceStatus(
    val chatService: ServiceStatus,
    val profileService: ServiceStatus
) {
    fun getBothServicesStatus(): ServiceStatus {
        val statusList = listOf(chatService, profileService)
        if (statusList.contains(ServiceStatus.CONNECTED) && statusList.contains(ServiceStatus.DISCONNECTED)) {
            return ServiceStatus.PARTIAL
        }

        if (statusList.all { it == ServiceStatus.CONNECTED }) {
            return ServiceStatus.CONNECTED
        }

        return ServiceStatus.DISCONNECTED
    }
}

sealed interface HomeUiState {
    val isLoading: Boolean
    val errorMessage: String
    val statistics: Map<String, PhishingStatsSummary>?
    val accessibilityServiceStatus: AccessibilityServiceStatus

    data class Default(
        override val isLoading: Boolean,
        override val errorMessage: String,
        override val statistics: Map<String, PhishingStatsSummary>? = null,
        override val accessibilityServiceStatus: AccessibilityServiceStatus = AccessibilityServiceStatus(
            ServiceStatus.DISCONNECTED,
            ServiceStatus.DISCONNECTED
        )
    ) : HomeUiState

}

private data class HomeViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val statistics: Map<String, PhishingStatsSummary>? = null,
    val accessibilityServiceStatus: AccessibilityServiceStatus = AccessibilityServiceStatus(
        ServiceStatus.DISCONNECTED,
        ServiceStatus.DISCONNECTED
    )
) {
    fun toUiState(): HomeUiState =
        HomeUiState.Default(
            isLoading = isLoading,
            errorMessage = errorMessage,
            statistics = statistics,
            accessibilityServiceStatus = accessibilityServiceStatus
        )

}

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val broadcastService: BroadcastService
) : ViewModel() {
    private val accessibilityReceiver: BroadcastReceiver
    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            isLoading = false,
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
        accessibilityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateServiceStatus(context, intent)
            }
        }

        val filter = IntentFilter().apply {
            addAction("com.phishbusters.clients.ACCESSIBILITY_CONNECTED")
            addAction("com.phishbusters.clients.ACCESSIBILITY_DISCONNECTED")
        }

        broadcastService.registerReceiver(accessibilityReceiver, filter);

        initServiceStatus()
        getUsersStatistics()
    }

    override fun onCleared() {
        super.onCleared()
        broadcastService.unregisterReceiver(accessibilityReceiver)
    }

    private fun getUsersStatistics() {
        viewModelState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val stats = homeRepository.getPhishingStatistics()
            viewModelState.update {
                it.copy(
                    isLoading = false,
                    statistics = stats
                )
            }
        }
    }

    private fun initServiceStatus() {
        broadcastService.getInitialServiceStatus()
    }

    private fun updateServiceStatus(context: Context?, intent: Intent?) {
        val serviceName = intent?.getStringExtra("service_name")
        val status = intent?.getStringExtra("status")
        when (serviceName) {
            "ChatAccessibilityService" -> {
                viewModelState.update {
                    it.copy(
                        accessibilityServiceStatus = it.accessibilityServiceStatus.copy(
                            chatService = if ("CONNECTED" == status) ServiceStatus.CONNECTED else ServiceStatus.DISCONNECTED
                        )
                    )
                }
            }

            "ProfileAccessibilityService" -> {
                viewModelState.update {
                    it.copy(
                        accessibilityServiceStatus = it.accessibilityServiceStatus.copy(
                            profileService = if ("CONNECTED" == status) ServiceStatus.CONNECTED else ServiceStatus.DISCONNECTED
                        )
                    )
                }
            }
        }
    }


    /**
     * Factory for HomeViewModel
     */
    companion object {
        fun provideFactory(
            homeRepository: HomeRepository,
            broadcastService: BroadcastService
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(homeRepository, broadcastService) as T
            }
        }
    }
}