package com.globant.ticketmaster.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.globant.ticketmaster.core.common.EventType
import java.util.Date

@Entity(
    tableName = "favoritesEvents",
)
data class FavoritesEventEntity(
    @PrimaryKey val idFavoriteEvent: String,
    val eventType: EventType,
    val createdAt: Long = Date().time,
)
