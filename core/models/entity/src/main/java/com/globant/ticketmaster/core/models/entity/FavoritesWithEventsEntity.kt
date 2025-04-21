package com.globant.ticketmaster.core.models.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FavoritesWithEventsEntity(
    @Embedded val favorites: FavoritesEventEntity,
    @Relation(
        parentColumn = "idFavoriteEvent",
        entityColumn = "idEvent",
    )
    val event: EventEntity,
    @Relation(
        parentColumn = "idFavoriteEvent",
        entityColumn = "idEventVenues",
    )
    val venues: List<VenuesEntity>,
)
