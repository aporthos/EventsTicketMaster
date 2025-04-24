package com.globant.ticketmaster.core.testing

import androidx.paging.PagingData
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

class FakeEventsRepository : EventsRepository {
    private val events: MutableSharedFlow<List<Event>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getLastVisitedEvents(countryCode: String): Flow<List<Event>> = events

    override fun getLastVisitedEventsPaging(
        keyword: String,
        countryCode: String,
    ): Flow<PagingData<Event>> {
        TODO("Not yet implemented")
    }

    override fun getFavoritesEventsPaging(
        keyword: String,
        countryCode: String,
    ): Flow<PagingData<Event>> {
        TODO("Not yet implemented")
    }

    override fun getEventsPaging(
        countryCode: String,
        keyword: String,
        idClassification: String,
    ): Flow<PagingData<Event>> {
        TODO("Not yet implemented")
    }

    override fun getDetailEvent(idEvent: String): Flow<Event> = events.map { it.first() }

    override suspend fun setFavoriteEvent(
        idEvent: String,
        eventType: EventType,
    ): Boolean = true

    override suspend fun setLastVisitedEvent(
        idEvent: String,
        lastVisited: Long,
        countryCode: String,
    ): Boolean = true

    fun addEvents(event: List<Event>) {
        events.tryEmit(event)
    }
}
