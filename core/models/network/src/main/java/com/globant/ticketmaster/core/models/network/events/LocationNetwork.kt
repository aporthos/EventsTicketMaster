package com.globant.ticketmaster.core.models.network.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationNetwork(
    @Json(name = "latitude")
    val latitude: String?,
    @Json(name = "longitude")
    val longitude: String?,
)
