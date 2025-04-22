package com.globant.ticketmaster.core.models.domain

data class Venues(
    val idVenue: String,
    val name: String,
    val urlVenue: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val country: String,
    val address: String,
    val location: Location,
)
