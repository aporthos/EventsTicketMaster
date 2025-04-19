package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.datasources.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.EventsRemoteDataSource
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class EventsRepositoryImpl
    @Inject
    constructor(
        private val local: EventsLocalDataSource,
        private val remote: EventsRemoteDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : EventsRepository {
        override fun getEvents(countryCode: String): Flow<List<Event>> =
            flow {
                val localEvents = local.getEvents()

                // TODO: Fix sync remote/local
//                if (localEvents.first().isEmpty()) {
                val result = remote.getEvents(countryCode)
                result
                    .onSuccess {
                        Timber.i("getEvents ${it.size}")
                        local.addEvents(it)
                    }.onFailure {
                        Timber.e("getEvents -> $it")
                    }
//                }
                emitAll(localEvents)
            }.flowOn(ioDispatcher)

        override fun getFavoritesEvents(): Flow<List<Event>> = local.getFavoritesEvents()

        override suspend fun setFavoriteEvent(
            idEvent: String,
            eventType: EventType,
        ): Boolean = local.setFavoriteEvent(idEvent, eventType)
    }
