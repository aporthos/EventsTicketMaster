package com.globant.ticketmaster.core.models.network.events

import com.globant.ticketmaster.core.models.network.EmbeddedNetwork
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventNetwork(
    @Json(name = "id")
    val idEvent: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "test")
    val test: Boolean?,
    @Json(name = "url")
    val urlEvent: String?,
    @Json(name = "locale")
    val locale: String?,
    @Json(name = "images")
    val urlImages: List<ImageNetwork>?,
    @Json(name = "dates")
    val dates: DatesNetwork?,
    @Json(name = "_embedded")
    val embedded: EmbeddedNetwork<VenuesNetwork>,
)
