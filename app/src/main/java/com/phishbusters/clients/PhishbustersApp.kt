package com.phishbusters.clients

import android.app.Application
import com.phishbusters.clients.data.AppContainer
import com.phishbusters.clients.data.AppContainerImpl

class PhishbustersApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
