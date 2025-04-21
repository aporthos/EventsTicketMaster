package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventEntity
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedWithEventsEntity
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
        page = event.page,
    )

fun LastVisitedWithEventsEntity.entityToDomain(): Event =
    Event(
        idEvent = event.idEvent,
        name = event.name,
        type = event.type,
        urlEvent = event.urlEvent,
        locale = event.locale,
        urlImage = event.urlImage,
        startDateTime = event.startDateTime,
        venues = emptyList(),
        eventType = event.eventType,
        countryCode = event.countryCode,
        idClassification = event.idClassification,
        page = event.page,
    )

fun EventEntity.entityToDomain(): Event =
    Event(
        idEvent = idEvent,
        name = name,
        type = type,
        urlEvent = urlEvent,
        locale = locale,
        urlImage = urlImage,
        startDateTime = startDateTime,
        venues = listOf(),
        eventType = eventType,
        countryCode = countryCode,
        idClassification = idClassification,
        page = page,
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
        page = page,
    )

fun EventNetwork.networkToDomain(
    eventType: EventType,
    countryCode: String,
    page: Int,
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
        page = page,
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
    page: Int,
): List<Event> =
    map { event ->
        event.networkToDomain(eventType = eventType, countryCode = countryCode, page = page)
    }

@JvmName("EventsWithVenuesEntity")
fun List<EventsWithVenuesEntity>.entityToDomains(): List<Event> = map(EventsWithVenuesEntity::entityToDomain)

@JvmName("lastVisitedWithEventsEntity")
fun List<LastVisitedWithEventsEntity>.entityToDomains(): List<Event> = map(LastVisitedWithEventsEntity::entityToDomain)

fun List<Event>.domainToEntities(): List<EventEntity> = map(Event::domainToEntity)
