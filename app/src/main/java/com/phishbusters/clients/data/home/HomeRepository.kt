package com.phishbusters.clients.data.home

import com.phishbusters.clients.model.Company
import com.phishbusters.clients.network.ApiResult

interface HomeRepository {
    suspend fun getCompanies(): ApiResult<List<Company>>
}