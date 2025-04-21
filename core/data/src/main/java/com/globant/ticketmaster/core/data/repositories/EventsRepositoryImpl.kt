package com.globant.ticketmaster.core.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsRemoteMediator
import com.globant.ticketmaster.core.data.mappers.entityToDomain
import com.globant.ticketmaster.core.database.EventsTransactions
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class EventsRepositoryImpl
    @Inject
    constructor(
        private val eventsDao: EventsDao,
        private val venuesDao: VenuesDao,
        private val eventsTransactions: EventsTransactions,
        private val local: EventsLocalDataSource,
        private val remote: EventsRemoteDataSource,
        private val apiServices: ApiServices,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : EventsRepository {
        override fun getEvents(
            countryCode: String,
            keyword: String,
            page: Int,
            idClassification: String,
        ): Flow<List<Event>> =
            flow {
                val localEvents =
                    local.getEvents(
                        countryCode = countryCode,
                        keyword = keyword,
                        idClassification = idClassification,
                    )

                // TODO: Fix sync remote/local
//                if (localEvents.first().isEmpty()) {
                val result =
                    remote.getEvents(
                        countryCode = countryCode,
                        keyword = keyword,
                        page = page,
                        idClassification = idClassification,
                    )
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

        override fun getEventsPaging(
            countryCode: String,
            keyword: String,
            idClassification: String,
        ): Flow<PagingData<Event>> =
            Pager(
                config = PagingConfig(pageSize = 20),
                remoteMediator =
                    EventsRemoteMediator(
                        countryCode = countryCode,
                        keyword = keyword,
                        idClassification = idClassification,
                        apiServices = apiServices,
                        eventsTransactions = eventsTransactions,
                        eventsDao = eventsDao,
                        venuesDao = venuesDao,
                    ),
                pagingSourceFactory = {
                    eventsDao.pagingAllEvents(
                        countryCode = countryCode,
                        keyword = search(keyword),
                        idClassification = search(idClassification),
                    )
                },
            ).flow.map {
                it.map(EventEntity::entityToDomain)
            }

        override fun getDetailEvent(idEvent: String): Flow<Event> = local.getDetailEvent(idEvent)

        override fun getFavoritesEvents(): Flow<List<Event>> = local.getFavoritesEvents()

        override suspend fun setFavoriteEvent(
            idEvent: String,
            eventType: EventType,
        ): Boolean = local.setFavoriteEvent(idEvent, eventType)

        private fun search(keyword: String): String =
            if (keyword.isEmpty()) {
                "%%"
            } else {
                "%$keyword%"
            }
    }
