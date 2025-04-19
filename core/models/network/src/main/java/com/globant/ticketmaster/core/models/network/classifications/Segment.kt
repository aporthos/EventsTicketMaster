package com.globant.ticketmaster.core.models.network.classifications

import com.globant.ticketmaster.core.models.network.LinksNetwork
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Segment(
    @Json(name = "_links")
    val links: LinksNetwork,
    @Json(name = "id")
    val idSegment: String,
    @Json(name = "locale")
    val locale: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "primaryId")
    val primaryId: String,
)
