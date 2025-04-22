package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.models.domain.Sales
import com.globant.ticketmaster.core.models.entity.SalesEntity
import com.globant.ticketmaster.core.models.network.events.SalesNetwork

fun SalesNetwork.networkToDomain(): Sales =
    Sales(
        startDateTime = public?.startDateTime?.toLongDate() ?: 0,
        endDateTime = public?.endDateTime?.toLongDate() ?: 0,
    )

fun Sales.domainToEntity(): SalesEntity = SalesEntity(startDateTime = startDateTime, endDateTime = endDateTime)

fun SalesEntity.entityToDomain(): Sales = Sales(startDateTime = startDateTime, endDateTime = endDateTime)
