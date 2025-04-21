package com.globant.ticketmaster.core.models.entity

import androidx.room.Embedded
import androidx.room.Relation

data class LastVisitedWithEventsEntity(
    @Embedded val lastVisited: LastVisitedEventEntity,
    @Relation(
        parentColumn = "idLastVisitedEvent",
        entityColumn = "idEvent",
    )
    val event: EventEntity,
)
