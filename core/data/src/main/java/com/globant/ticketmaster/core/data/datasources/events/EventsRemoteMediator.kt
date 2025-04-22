package com.globant.ticketmaster.core.data.datasources.events

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.mappers.domainToEntities
import com.globant.ticketmaster.core.data.mappers.networkToDomains
import com.globant.ticketmaster.core.database.EventsTransactions
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.network.events.EventNetwork
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class EventsRemoteMediator(
    private val countryCode: String,
    private val keyword: String,
    private val idClassification: String,
    private val apiServices: ApiServices,
    private val eventsTransactions: EventsTransactions,
    private val eventsDao: EventsDao,
    private val venuesDao: VenuesDao,
) : RemoteMediator<Int, EventsWithVenuesEntity>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 0
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventsWithVenuesEntity>,
    ): MediatorResult {
        return try {
            val page =
                when (loadType) {
                    LoadType.REFRESH -> STARTING_PAGE_INDEX
                    LoadType.PREPEND -> return MediatorResult.Success(
                        endOfPaginationReached = true,
                    )

                    LoadType.APPEND -> {
                        val lastItem =
                            state.lastItemOrNull()
                                ?: return MediatorResult.Success(
                                    endOfPaginationReached = false,
                                )
                        lastItem.event.page + 1
                    }
                }
            val response =
                apiServices.getEvents(
                    countryCode = countryCode,
                    keyword = keyword,
                    page = page,
                    idClassification = idClassification,
                )
            // TODO: Remove, this only for tests
            delay(3_000L)
            response.embedded?.events?.let { events ->
                executeTransactions(loadType, page, events)
            }
            MediatorResult.Success(
                endOfPaginationReached = response.page?.totalPages == page,
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun executeTransactions(
        loadType: LoadType,
        page: Int,
        eventsNetwork: List<EventNetwork>,
    ) {
        val events = eventsNetwork.networkToDomains(countryCode = countryCode, page = page)
        eventsTransactions.deleteInsert(
            loadType,
            needDelete = { loadType == LoadType.REFRESH },
            deleteOperation = {
                val venues =
                    events
                        .flatMap { it.venues.domainToEntities(it.idEvent) }
                        .map { it.idEventVenues }
                eventsDao.deleteEventsById(events.map { it.idEvent })
                venuesDao.deleteVenuesByIdEvent(venues)
            },
            insertOperation = {
                val venues = events.flatMap { it.venues.domainToEntities(it.idEvent) }
                eventsDao.insertOrIgnore(events.domainToEntities())
                venuesDao.insertOrIgnore(venues)
            },
        )
    }
}
