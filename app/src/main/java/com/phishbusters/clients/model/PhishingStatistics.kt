package com.phishbusters.clients.model

import java.util.Date

data class PhishingStatistics(
    val date: Date,
    val phishingChatsDetected: Int,
    val fakeProfilesDetected: Int,
)
