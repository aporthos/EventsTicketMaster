package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventEntity
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.network.events.EventNetwork

fun EventsWithVenuesEntity.entityToDomain(): Event =
    Event(
        idEvent = event.idEvent,
        name = event.name,
        type = event.type,
        urlEvent = event.urlEvent,
        locale = event.locale,
        urlImage = event.urlImage,
        startDateTime = event.startDateTime,
        venues = venues.entityToDomains(),
        eventType = event.eventType,
        countryCode = event.countryCode,
        idClassification = event.idClassification,
    )

fun Event.domainToEntity(): EventEntity =
    EventEntity(
        idEvent = idEvent,
        name = name,
        type = type,
        urlEvent = urlEvent,
        locale = locale,
        urlImage = urlImage,
        startDateTime = startDateTime,
        eventType = eventType,
        countryCode = countryCode,
        idClassification = idClassification,
    )

fun EventNetwork.networkToDomain(
    eventType: EventType,
    countryCode: String,
): Event =
    Event(
        idEvent = idEvent.orEmpty(),
        name = name.orEmpty(),
        type = type.orEmpty(),
        urlEvent = urlEvent.orEmpty(),
        locale = locale.orEmpty(),
        urlImage = urlImages?.firstOrNull()?.url ?: "",
        startDateTime = dates?.start?.dateTime.orEmpty(),
        venues = embedded.venues.networkToDomains(),
        eventType = eventType,
        countryCode = countryCode,
        idClassification =
            classifications
                ?.firstOrNull()
                ?.segment
                ?.id
                .orEmpty(),
    )

fun List<EventNetwork>.networkToDomains(
    eventType: EventType = EventType.Default,
    countryCode: String,
): List<Event> =
    map { event ->
        event.networkToDomain(eventType, countryCode)
    }

fun List<EventsWithVenuesEntity>.entityToDomains(): List<Event> = map(EventsWithVenuesEntity::entityToDomain)

fun List<Event>.domainToEntities(): List<EventEntity> = map(Event::domainToEntity)
