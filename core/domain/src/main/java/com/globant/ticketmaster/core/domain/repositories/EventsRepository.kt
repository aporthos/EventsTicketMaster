package com.globant.ticketmaster.core.domain.repositories

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getEvents(countryCode: String): Flow<List<Event>>

    fun getFavoritesEvents(): Flow<List<Event>>

    suspend fun setFavoriteEvent(
        idEvent: String,
        eventType: EventType,
    ): Boolean
}
