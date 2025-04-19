package com.globant.ticketmaster.core.models.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponseNetwork<T>(
    @Json(name = "_embedded")
    val embedded: EmbeddedNetwork<T>,
    @Json(name = "_links")
    val links: LinksNetwork?,
    @Json(name = "page")
    val page: PageNetwork?,
)
