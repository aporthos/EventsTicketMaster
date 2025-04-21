package com.globant.ticketmaster.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "lastVisitedEvents",
)
data class LastVisitedEventEntity(
    @PrimaryKey val idLastVisitedEvent: String,
    val cratedAt: Long,
)
