package com.globant.ticketmaster.core.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsRemoteMediator
import com.globant.ticketmaster.core.data.mappers.entityToDomain
import com.globant.ticketmaster.core.database.EventsTransactions
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.entity.FavoritesWithEventsEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedWithEventsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventsRepositoryImpl
    @Inject
    constructor(
        private val eventsDao: EventsDao,
        private val eventsTransactions: EventsTransactions,
        private val local: EventsLocalDataSource,
        private val apiServices: ApiServices,
        private val pagerConfig: PagingConfig,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : EventsRepository {
        override fun getLastVisitedEvents(countryCode: String): Flow<List<Event>> = local.getLastVisitedEvents(countryCode)

        override fun getLastVisitedEventsPaging(
            keyword: String,
            countryCode: String,
        ): Flow<PagingData<Event>> =
            Pager(
                config = pagerConfig,
                pagingSourceFactory = {
                    local.getLastVisitedEventsPaging(keyword, countryCode)
                },
            ).flow
                .map {
                    it.map(LastVisitedWithEventsEntity::entityToDomain)
                }.flowOn(dispatcher)

        override fun getFavoritesEventsPaging(
            keyword: String,
            countryCode: String,
        ): Flow<PagingData<Event>> =
            Pager(
                config = pagerConfig,
                pagingSourceFactory = {
                    local.getFavoritesEventsPaging(keyword, countryCode)
                },
            ).flow
                .map {
                    it.map(FavoritesWithEventsEntity::entityToDomain)
                }.flowOn(dispatcher)

        override fun getEventsPaging(
            countryCode: String,
            keyword: String,
            idClassification: String,
        ): Flow<PagingData<Event>> =
            Pager(
                config = pagerConfig,
                remoteMediator =
                    EventsRemoteMediator(
                        countryCode = countryCode,
                        keyword = keyword,
                        idClassification = idClassification,
                        apiServices = apiServices,
                        eventsTransactions = eventsTransactions,
                        eventsLocalDataSource = local,
                    ),
                pagingSourceFactory = {
                    eventsDao.pagingAllEvents(
                        countryCode = countryCode,
                        keyword = keyword,
                        idClassification = idClassification,
                    )
                },
            ).flow
                .map {
                    it.map(EventsWithVenuesEntity::entityToDomain)
                }.flowOn(dispatcher)

        override fun getDetailEvent(idEvent: String): Flow<Event> = local.getDetailEvent(idEvent)

        override suspend fun setFavoriteEvent(
            idEvent: String,
            eventType: EventType,
        ): Boolean = local.setFavoriteEvent(idEvent, eventType)

        override suspend fun setLastVisitedEvent(
            idEvent: String,
            lastVisited: Long,
            countryCode: String,
        ): Boolean =
            local.setLastVisitedEvent(
                idEvent = idEvent,
                lastVisited = lastVisited,
                countryCode = countryCode,
            )
    }
