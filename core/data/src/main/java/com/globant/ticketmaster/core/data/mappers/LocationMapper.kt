package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.entity.LocationEntity
import com.globant.ticketmaster.core.models.network.events.LocationNetwork

fun LocationNetwork.networkToDomain(): Location = Location(latitude = latitude.toDouble(), longitude = longitude.toDouble())

fun LocationNetwork.networkToEntity(): LocationEntity = LocationEntity(latitude = latitude.toDouble(), longitude = longitude.toDouble())

fun Location.domainToEntity(): LocationEntity = LocationEntity(latitude = latitude, longitude = longitude)

fun LocationEntity.entityToDomain(): Location = Location(latitude = latitude, longitude = longitude)
