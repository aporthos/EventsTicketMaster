package com.globant.ticketmaster.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globant.ticketmaster.core.common.EventType

@Entity(
    tableName = "events",
)
data class EventEntity(
    @PrimaryKey val idEvent: String,
    val name: String,
    val type: String,
    val urlEvent: String,
    val locale: String,
    val urlImage: String,
    val startDateTime: String,
    val eventType: EventType,
    val countryCode: String,
    val idClassification: String,
)
