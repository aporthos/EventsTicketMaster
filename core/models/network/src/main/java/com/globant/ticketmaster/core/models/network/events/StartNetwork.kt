package com.globant.ticketmaster.core.models.network.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StartNetwork(
    @Json(name = "dateTBA")
    val dateTba: Boolean,
    @Json(name = "dateTBD")
    val dateTbd: Boolean,
    @Json(name = "dateTime")
    val dateTime: String,
    @Json(name = "localDate")
    val localDate: String,
    @Json(name = "localTime")
    val localTime: String,
    @Json(name = "noSpecificTime")
    val noSpecificTime: Boolean,
    @Json(name = "timeTBA")
    val timeTba: Boolean,
)
