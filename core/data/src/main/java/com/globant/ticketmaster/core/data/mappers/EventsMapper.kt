package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.domain.Sales
import com.globant.ticketmaster.core.models.entity.EventEntity
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.entity.FavoritesWithEventsEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedWithEventsEntity
import com.globant.ticketmaster.core.models.network.events.EventNetwork
import java.text.SimpleDateFormat
import java.util.Locale

fun EventsWithVenuesEntity.entityToDomain(): Event =
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
        segment = event.segment,
        seatMap = event.seatMap,
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

fun FavoritesWithEventsEntity.entityToDomain(): Event =
    Event(
        idEvent = event.idEvent,
        name = event.name,
        type = event.type,
        urlEvent = event.urlEvent,
        locale = event.locale,
        urlImage = event.urlImage,
        startEventDateTime = event.startEvent,
        venues = venues.entityToDomains(),
        eventType = favorites.eventType,
        countryCode = event.countryCode,
        idClassification = event.idClassification,
        sales = event.sales.entityToDomain(),
        info = event.info,
        segment = event.segment,
        seatMap = event.seatMap,
        page = event.page,
    )

fun Event.domainToEntity(): EventEntity =
    EventEntity(
        idEvent = idEvent,
        name = name,
        type = type,
        urlEvent = urlEvent,
        locale = locale,
        urlImage = urlImage,
        startEvent = startEventDateTime,
        countryCode = countryCode,
        idClassification = idClassification,
        info = info,
        sales = sales.domainToEntity(),
        segment = segment,
        seatMap = seatMap,
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
        urlImage = urlImages?.firstOrNull { it.width == 1024 }?.url.orEmpty(),
        startEventDateTime = dates?.start?.localDate?.toLongDate() ?: 0,
        venues = embedded.venues.networkToDomains(),
        eventType = eventType,
        countryCode = countryCode,
        page = page,
        info = info.orEmpty(),
        seatMap = seatMap?.staticUrl.orEmpty(),
        segment =
            classifications
                ?.firstOrNull()
                ?.segment
                ?.name
                .orEmpty(),
        sales = sales?.networkToDomain() ?: Sales(0, 0),
        idClassification =
            classifications
                ?.firstOrNull()
                ?.segment
                ?.id
                .orEmpty(),
    )

fun String.toLongDate(): Long =
    try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(this)?.time ?: 0
    } catch (e: Exception) {
        0
    }

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
