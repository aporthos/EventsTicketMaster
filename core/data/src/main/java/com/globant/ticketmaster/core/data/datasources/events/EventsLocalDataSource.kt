package com.globant.ticketmaster.core.data.datasources.events

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.mappers.domainToEntities
import com.globant.ticketmaster.core.data.mappers.entityToDomain
import com.globant.ticketmaster.core.data.mappers.entityToDomains
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.LastVisitedEventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedEventEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedWithEventsEntity
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
        private val lastVisitedEventsDao: LastVisitedEventsDao,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : EventsLocalDataSource {
        override fun getLastVisitedEvents(countryCode: String): Flow<List<Event>> =
            lastVisitedEventsDao
                .getLastVisitedEvents(countryCode)
                .catch {
                    throw it
                }.map(List<LastVisitedWithEventsEntity>::entityToDomains)
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

        override suspend fun setLastVisitedEvent(
            idEvent: String,
            lastVisited: Long,
            countryCode: String,
        ): Boolean =
            withContext(dispatcher) {
                lastVisitedEventsDao.insertOrIgnore(
                    LastVisitedEventEntity(
                        idLastVisitedEvent = idEvent,
                        countryCode = countryCode,
                        lastVisited = lastVisited,
                    ),
                ) > 0
            }
    }

interface EventsLocalDataSource {
    fun getLastVisitedEvents(countryCode: String): Flow<List<Event>>

    fun getDetailEvent(idEvent: String): Flow<Event>

    fun getFavoritesEvents(): Flow<List<Event>>

    fun getSuggestedEvents(ids: List<String>): Flow<List<Event>>

    suspend fun addEvents(events: List<Event>)

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
