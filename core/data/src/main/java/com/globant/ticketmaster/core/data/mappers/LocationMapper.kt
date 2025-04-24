package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.entity.LocationEntity
import com.globant.ticketmaster.core.models.network.events.LocationNetwork

fun LocationNetwork.networkToDomain(): Location = Location(latitude = latitude?.toDouble() ?: 0.0, longitude = longitude?.toDouble() ?: 0.0)

fun Location.domainToEntity(): LocationEntity = LocationEntity(latitude = latitude, longitude = longitude)

fun LocationEntity.entityToDomain(): Location = Location(latitude = latitude, longitude = longitude)
