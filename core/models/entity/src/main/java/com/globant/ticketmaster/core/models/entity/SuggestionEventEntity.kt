package com.globant.ticketmaster.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "suggestionsEvents",
)
data class SuggestionEventEntity(
    @PrimaryKey val idEvent: String,
    val createdAt: Long = Date().time,
)
