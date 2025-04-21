package com.globant.ticketmaster.core.models.domain

import com.globant.ticketmaster.core.common.EventType

data class Event(
    val idEvent: String,
    val name: String,
    val type: String,
    val urlEvent: String,
    val locale: String,
    val urlImage: String,
    val startDateTime: String,
    val venues: List<Venues>,
    val eventType: EventType,
    val countryCode: String,
    val idClassification: String,
    val page: Int,
)
