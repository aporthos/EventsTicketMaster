package com.globant.ticketmaster.core.domain.repositories

import androidx.paging.PagingData
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getEvents(
        countryCode: String,
        keyword: String,
        page: Int,
        idClassification: String,
    ): Flow<List<Event>>

    fun getEventsPaging(
        countryCode: String,
        keyword: String,
        idClassification: String,
    ): Flow<PagingData<Event>>

    fun getDetailEvent(idEvent: String): Flow<Event>

    fun getFavoritesEvents(): Flow<List<Event>>

    suspend fun setFavoriteEvent(
        idEvent: String,
        eventType: EventType,
    ): Boolean
}
