package com.phishbusters.clients.data.home

import com.google.gson.reflect.TypeToken
import com.phishbusters.clients.data.ConfigVars
import com.phishbusters.clients.model.CombinedPhishingStats
import com.phishbusters.clients.model.PhishingStatistics
import com.phishbusters.clients.model.PhishingStatisticsResponse
import com.phishbusters.clients.network.ApiResult
import com.phishbusters.clients.network.ApiService
import com.phishbusters.clients.network.HttpMethod
import com.phishbusters.clients.ui.home.PhishingStatsSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat


class HomeRepositoryImpl(private val apiService: ApiService) : HomeRepository {

    override suspend fun getPhishingStatistics(): ApiResult<CombinedPhishingStats> =
        withContext(
            Dispatchers.IO
        ) {
            val apiEndpoint = ConfigVars.API_URL + "/phishing-stats"
            val type = object : TypeToken<PhishingStatisticsResponse>() {}.type
            val apiResult = apiService.suspendHttpCall<PhishingStatisticsResponse>(
                url = apiEndpoint,
                type = type,
                method = HttpMethod.GET,
            )

            return@withContext when (apiResult) {
                is ApiResult.Success -> {
                    val lastSevenDays = processPhishingStatistics(apiResult.data.lastSevenDays)
                    val sinceCreation = apiResult.data.sinceCreation
                    ApiResult.Success(CombinedPhishingStats(lastSevenDays, sinceCreation))
                }

                is ApiResult.Error -> {
                    ApiResult.Error(apiResult.exception)
                }
            }
        }

    private fun processPhishingStatistics(phishingStats: List<PhishingStatistics>): Map<String, PhishingStatsSummary> {
        val sdf = SimpleDateFormat("dd/MM/yyyy")

        return phishingStats.groupBy {
            sdf.format(it.date)
        }.mapValues { entry ->
            val summary = PhishingStatsSummary(
                phishingChatsDetected = entry.value.sumOf { it.phishingChatsDetected },
                fakeProfilesDetected = entry.value.sumOf { it.fakeProfilesDetected }
            )
            summary
        }
    }
}
