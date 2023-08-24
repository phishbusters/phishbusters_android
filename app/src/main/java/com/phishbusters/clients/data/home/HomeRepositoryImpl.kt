package com.phishbusters.clients.data.home

import com.phishbusters.clients.data.ConfigVars
import com.phishbusters.clients.model.Company
import com.phishbusters.clients.network.ApiResult
import com.phishbusters.clients.network.ApiService
import com.phishbusters.clients.network.HttpMethod
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class HomeRepositoryImpl(private val apiService: ApiService) : HomeRepository {
    override suspend fun getCompanies(): ApiResult<List<Company>> {
        val endpointUrl = ConfigVars.API_URL + "/companies"
        return suspendCoroutine { continuation ->
            apiService.httpCall(endpointUrl, HttpMethod.GET) { result: ApiResult<List<Company>> ->
                continuation.resume(result)
            }
        }
    }
}
