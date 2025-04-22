package com.globant.ticketmaster.core.models.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val startEvent: Long,
    val countryCode: String,
    val idClassification: String,
    val info: String,
    val segment: String,
    val page: Int,
    @Embedded val sales: SalesEntity,
)
