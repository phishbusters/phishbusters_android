package com.phishbusters.clients.data

import android.content.Context
import com.phishbusters.clients.data.analyze.AnalyzeRepository
import com.phishbusters.clients.data.analyze.AnalyzeRepositoryImpl
import com.phishbusters.clients.data.auth.AuthRepository
import com.phishbusters.clients.data.auth.AuthRepositoryImpl
import com.phishbusters.clients.data.home.HomeRepository
import com.phishbusters.clients.data.home.HomeRepositoryImpl
import com.phishbusters.clients.services.notifications.NotificationService
import com.phishbusters.clients.services.notifications.NotificationServiceImpl
import com.phishbusters.clients.data.settings.SettingsRepository
import com.phishbusters.clients.data.settings.SettingsRepositoryImpl
import com.phishbusters.clients.network.ApiService

interface AppContainer {
    val homeRepository: HomeRepository
    val settingsRepository: SettingsRepository
    val analyzeRepository: AnalyzeRepository
    val notificationService: NotificationService
    val authRepository: AuthRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    private val tokenStore = TokenStore(applicationContext)
    private val apiService = ApiService(tokenStore)

    override val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl(apiService)
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl()
    }

    override val analyzeRepository: AnalyzeRepository by lazy {
        AnalyzeRepositoryImpl(apiService, this.notificationService)
    }

    override val notificationService: NotificationService by lazy {
        NotificationServiceImpl(applicationContext)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(tokenStore)
    }
}
