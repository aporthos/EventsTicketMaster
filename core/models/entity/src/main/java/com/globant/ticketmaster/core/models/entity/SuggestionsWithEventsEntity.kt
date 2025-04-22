package com.globant.ticketmaster.core.models.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SuggestionsWithEventsEntity(
    @Embedded val suggestion: SuggestionEventEntity,
    @Relation(
        parentColumn = "idEvent",
        entityColumn = "idEvent",
    )
    val event: EventEntity,
    @Relation(
        parentColumn = "idEvent",
        entityColumn = "idEventVenues",
    )
    val venues: List<VenuesEntity>,
    @Relation(
        parentColumn = "idEvent",
        entityColumn = "idFavoriteEvent",
    )
    val favorite: FavoritesEventEntity?,
)
