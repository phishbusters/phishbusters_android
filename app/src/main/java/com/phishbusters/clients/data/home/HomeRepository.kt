package com.phishbusters.clients.data.home

import com.phishbusters.clients.network.ApiResult

interface HomeRepository {
    fun registerReceiver()

    fun unregisterReceiver()
}