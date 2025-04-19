package com.globant.ticketmaster.core.data.datasources

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.mappers.domainToEntities
import com.globant.ticketmaster.core.data.mappers.entityToDomains
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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
        override fun getEvents(): Flow<List<Event>> =
            eventsDao
                .getAllEvents()
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
    }

interface EventsLocalDataSource {
    fun getEvents(): Flow<List<Event>>

    fun getSuggestedEvents(ids: List<String>): Flow<List<Event>>

    suspend fun addEvents(events: List<Event>)
}
