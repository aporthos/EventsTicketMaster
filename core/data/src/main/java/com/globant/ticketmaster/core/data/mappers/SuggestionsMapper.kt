package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.SuggestionsWithEventsEntity

fun SuggestionsWithEventsEntity.entityToDomain() =
    Event(
        idEvent = event.idEvent,
        name = event.name,
        type = event.type,
        urlEvent = event.urlEvent,
        locale = event.locale,
        urlImage = event.urlImage,
        startEventDateTime = event.startEvent,
        venues = venues.entityToDomains(),
        eventType = favorite?.eventType ?: EventType.Default,
        countryCode = event.countryCode,
        idClassification = event.idClassification,
        sales = event.sales.entityToDomain(),
        info = event.info,
        seatMap = event.seatMap,
        segment = event.segment,
        page = event.page,
    )

@JvmName("SuggestionsWithEventsEntity")
fun List<SuggestionsWithEventsEntity>.entityToDomains(): List<Event> = map(SuggestionsWithEventsEntity::entityToDomain)
