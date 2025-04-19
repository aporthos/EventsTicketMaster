package com.globant.ticketmaster.core.models.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmbeddedNetwork<T>(
    val classifications: List<T> = emptyList(),
    val events: List<T> = emptyList(),
    val venues: List<T> = emptyList(),
)
