package com.globant.ticketmaster.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "suggestionsEvents",
)
data class SuggestionEventEntity(
    @PrimaryKey val idEvent: String,
)
