package com.globant.ticketmaster.core.models.network.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageNetwork(
    @Json(name = "attribution")
    val attribution: String?,
    @Json(name = "fallback")
    val fallback: Boolean?,
    @Json(name = "height")
    val height: Int?,
    @Json(name = "ratio")
    val ratio: String?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "width")
    val width: Int?,
)
