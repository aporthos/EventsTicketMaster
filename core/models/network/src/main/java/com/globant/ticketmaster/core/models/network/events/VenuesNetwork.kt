package com.globant.ticketmaster.core.models.network.events

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenuesNetwork(
    @Json(name = "address")
    val address: Address?,
    @Json(name = "city")
    val city: City?,
    @Json(name = "country")
    val country: Country?,
    @Json(name = "id")
    val idVenue: String?,
    @Json(name = "images")
    val images: List<ImageNetwork>?,
    @Json(name = "locale")
    val locale: String?,
    @Json(name = "location")
    val location: LocationNetwork?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "parkingDetail")
    val parkingDetail: String?,
    @Json(name = "postalCode")
    val postalCode: String?,
    @Json(name = "state")
    val state: State?,
    @Json(name = "test")
    val test: Boolean?,
    @Json(name = "timezone")
    val timezone: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "url")
    val urlVenue: String?,
)
