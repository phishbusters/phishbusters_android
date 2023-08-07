package com.phishbusters.clients

import android.app.Application
import com.phishbusters.clients.data.AppContainer
import com.phishbusters.clients.data.AppContainerImpl

class PhishbustersApp : Application() {
    companion object {
        const val API_URL = "https://phishbusters-api.herokuapp.com"
    }

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
