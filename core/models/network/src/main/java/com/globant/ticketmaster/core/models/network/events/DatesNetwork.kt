package com.globant.ticketmaster.core.models.network.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DatesNetwork(
    @Json(name = "spanMultipleDays")
    val spanMultipleDays: Boolean,
    @Json(name = "start")
    val start: StartNetwork,
    @Json(name = "status")
    val status: Status,
    @Json(name = "timezone")
    val timezone: String,
)
