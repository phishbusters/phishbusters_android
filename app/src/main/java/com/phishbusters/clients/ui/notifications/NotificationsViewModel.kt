package com.phishbusters.clients.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.phishbusters.clients.data.notification.NotificationRepository
import com.phishbusters.clients.model.NotificationData
import com.phishbusters.clients.model.NotificationsType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface NotificationUiState {
    val isLoading: Boolean
    val notifications: List<NotificationData>
    val errorMessage: String

    data class Default(
        override val isLoading: Boolean,
        override val notifications: List<NotificationData>,
        override val errorMessage: String
    ) : NotificationUiState
}

private data class NotificationViewModelState(
    val isLoading: Boolean,
    val notifications: List<NotificationData>,
    val errorMessage: String
) {
    fun toUiState(): NotificationUiState =
        NotificationUiState.Default(
            isLoading = isLoading,
            notifications = notifications,
            errorMessage = errorMessage
        )
}

class NotificationsViewModel(
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    private val viewModelState = MutableStateFlow(
        NotificationViewModelState(
            isLoading = true,
            emptyList(),
            errorMessage = ""
        )
    )

    val uiState = viewModelState
        .map(NotificationViewModelState::toUiState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())


    init {
        viewModelScope.launch {
            notificationRepository.notificationsFlow.collect { notifications ->
                if (notifications.isEmpty()) {
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            notifications = emptyList(),
                            errorMessage = "No hay notificaciones"
                        )
                    }
                } else {
                    viewModelState.update {
                        it.copy(
                            isLoading = false,
                            notifications = notifications,
                            errorMessage = ""
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            notificationRepository: NotificationRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotificationsViewModel(notificationRepository) as T
            }
        }
    }
}
