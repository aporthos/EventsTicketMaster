package com.globant.ticketmaster.core.data.datasources

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.mappers.domainToEntities
import com.globant.ticketmaster.core.data.mappers.entityToDomain
import com.globant.ticketmaster.core.data.mappers.entityToDomains
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventsLocalDataSourceImpl
    @Inject
    constructor(
        private val eventsDao: EventsDao,
        private val venuesDao: VenuesDao,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : EventsLocalDataSource {
        override fun getEvents(
            countryCode: String,
            keyword: String,
            idClassification: String,
        ): Flow<List<Event>> =
            eventsDao
                .getAllEvents(
                    countryCode = countryCode,
                    keyword = search(keyword),
                    idClassification = search(idClassification),
                ).map(List<EventsWithVenuesEntity>::entityToDomains)
                .flowOn(dispatcher)

        override fun getDetailEvent(idEvent: String): Flow<Event> =
            eventsDao
                .getEventById(idEvent)
                .map(EventsWithVenuesEntity::entityToDomain)
                .catch {
                    throw it
                }.flowOn(dispatcher)

        override fun getFavoritesEvents(): Flow<List<Event>> =
            eventsDao
                .getFavoritesEvents(EventType.Favorite)
                .map(List<EventsWithVenuesEntity>::entityToDomains)
                .flowOn(dispatcher)

        override fun getSuggestedEvents(ids: List<String>): Flow<List<Event>> =
            eventsDao
                .getSuggestedEvents(ids)
                .map(List<EventsWithVenuesEntity>::entityToDomains)
                .flowOn(dispatcher)

        override suspend fun addEvents(events: List<Event>) {
            withContext(dispatcher) {
                eventsDao.insertOrIgnore(events.domainToEntities())
                events.map { event ->
                    venuesDao.insertOrIgnore(event.venues.domainToEntities(event.idEvent))
                }
            }
        }

        override suspend fun setFavoriteEvent(
            idEvent: String,
            eventType: EventType,
        ): Boolean =
            withContext(dispatcher) {
                eventsDao.updateFavoriteByIdEvent(idEvent, eventType) > 0
            }

        private fun search(keyword: String): String =
            if (keyword.isEmpty()) {
                "%%"
            } else {
                "%$keyword%"
            }
    }

interface EventsLocalDataSource {
    fun getEvents(
        countryCode: String,
        keyword: String,
        idClassification: String,
    ): Flow<List<Event>>

    fun getDetailEvent(idEvent: String): Flow<Event>

    fun getFavoritesEvents(): Flow<List<Event>>

    fun getSuggestedEvents(ids: List<String>): Flow<List<Event>>

    suspend fun addEvents(events: List<Event>)

    suspend fun setFavoriteEvent(
        idEvent: String,
        eventType: EventType,
    ): Boolean
}
