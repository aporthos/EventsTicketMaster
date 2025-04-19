package com.globant.ticketmaster.core.models.network.classifications

import com.globant.ticketmaster.core.models.network.LinksNetwork
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ClassificationNetwork(
    @Json(name = "_links")
    val links: LinksNetwork,
    @Json(name = "family")
    val family: Boolean,
    @Json(name = "segment")
    val segment: Segment?,
)
