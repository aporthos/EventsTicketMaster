package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.domain.Venues
import com.globant.ticketmaster.core.models.entity.VenuesEntity
import com.globant.ticketmaster.core.models.network.events.VenuesNetwork

fun VenuesNetwork.networkToDomain(): Venues =
    Venues(
        idVenue = idVenue.orEmpty(),
        name = name.orEmpty(),
        urlVenue = urlVenue.orEmpty(),
        city = city?.name.orEmpty(),
        state = state?.name.orEmpty(),
        stateCode = state?.stateCode.orEmpty(),
        country = country?.name.orEmpty(),
        address = address?.address.orEmpty(),
        location = location?.networkToDomain() ?: Location(0.0, 0.0),
    )

fun Venues.domainToEntity(idEvent: String): VenuesEntity =
    VenuesEntity(
        idVenue = idVenue,
        idEventVenues = idEvent,
        name = name,
        urlVenue = urlVenue,
        city = city,
        state = state,
        stateCode = stateCode,
        country = country,
        address = address,
        location = location.domainToEntity(),
    )

fun VenuesEntity.entityToDomain(): Venues =
    Venues(
        idVenue = idVenue,
        name = name,
        urlVenue = urlVenue,
        city = city,
        state = state,
        stateCode = stateCode,
        country = country,
        address = address,
        location = location.entityToDomain(),
    )

fun List<VenuesNetwork>.networkToDomains(): List<Venues> = map(VenuesNetwork::networkToDomain)

fun List<VenuesEntity>.entityToDomains(): List<Venues> = map(VenuesEntity::entityToDomain)

fun List<Venues>.domainToEntities(idEvent: String): List<VenuesEntity> =
    map { venue ->
        venue.domainToEntity(idEvent = idEvent)
    }
