package com.phishbusters.clients.data.home

import com.phishbusters.clients.data.ConfigVars
import com.phishbusters.clients.network.ApiResult
import com.phishbusters.clients.network.ApiService
import com.phishbusters.clients.network.HttpMethod
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class HomeRepositoryImpl(private val apiService: ApiService) : HomeRepository {
    override fun registerReceiver() {
        TODO("Not yet implemented")
    }

    override fun unregisterReceiver() {
        TODO("Not yet implemented")
    }
}
