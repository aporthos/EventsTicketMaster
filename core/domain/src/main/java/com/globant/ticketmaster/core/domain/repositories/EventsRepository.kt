package com.globant.ticketmaster.core.domain.repositories

import androidx.paging.PagingData
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getLastVisitedEvents(countryCode: String): Flow<List<Event>>

    fun getLastVisitedEventsPaging(
        keyword: String,
        countryCode: String,
    ): Flow<PagingData<Event>>

    fun getFavoritesEventsPaging(
        keyword: String,
        countryCode: String,
    ): Flow<PagingData<Event>>

    fun getEventsPaging(
        countryCode: String,
        keyword: String,
        idClassification: String,
    ): Flow<PagingData<Event>>

    fun getDetailEvent(idEvent: String): Flow<Event>

    suspend fun setFavoriteEvent(
        idEvent: String,
        eventType: EventType,
    ): Boolean

    suspend fun setLastVisitedEvent(
        idEvent: String,
        lastVisited: Long,
        countryCode: String,
    ): Boolean
}
