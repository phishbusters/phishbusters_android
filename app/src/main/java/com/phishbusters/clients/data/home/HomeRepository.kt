package com.phishbusters.clients.data.home

import com.phishbusters.clients.network.ApiResult
import com.phishbusters.clients.ui.home.PhishingStatsSummary

interface HomeRepository {
    suspend fun getPhishingStatistics(): ApiResult<Map<String, PhishingStatsSummary>>
}