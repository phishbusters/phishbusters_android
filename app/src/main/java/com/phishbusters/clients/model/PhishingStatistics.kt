package com.phishbusters.clients.model

import com.phishbusters.clients.ui.home.PhishingStatsSummary
import java.util.Date

data class PhishingStatisticsResponse(
    val lastSevenDays: List<PhishingStatistics>,
    val sinceCreation: TotalStats
)

data class CombinedPhishingStats(
    val lastSevenDays: Map<String, PhishingStatsSummary>,
    val sinceCreation: TotalStats
)

data class TotalStats(
    val totalPhishingChats: Int,
    val totalFakeProfiles: Int,
    val totalComplaints: Int,
)

data class PhishingStatistics(
    val date: Date,
    val phishingChatsDetected: Int,
    val fakeProfilesDetected: Int,
)
