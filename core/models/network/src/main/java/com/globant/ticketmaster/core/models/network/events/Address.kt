package com.globant.ticketmaster.core.models.network.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "line1")
    val address: String?,
)
