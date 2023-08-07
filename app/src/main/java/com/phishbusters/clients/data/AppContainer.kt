package com.phishbusters.clients.data

import android.content.Context
import com.phishbusters.clients.data.home.HomeRepository
import com.phishbusters.clients.data.home.HomeRepositoryImpl
import com.phishbusters.clients.data.settings.SettingsRepository
import com.phishbusters.clients.data.settings.SettingsRepositoryImpl

interface AppContainer {
    val homeRepository: HomeRepository
    val settingsRepository: SettingsRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {
    override val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl()
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepositoryImpl()
    }
}
