package com.globant.ticketmaster.core.data.datasources

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.mappers.networkToDomains
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class EventsRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiServices: ApiServices,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : EventsRemoteDataSource {
        override suspend fun getEvents(countryCode: String): Result<List<Event>> =
            withContext(ioDispatcher) {
                try {
                    val result = apiServices.getEvents(countryCode)
                    Result.success(result.embedded.events.networkToDomains())
                } catch (e: Exception) {
                    Timber.e("getEvents -> $e")
                    Result.failure(e)
                }
            }
    }

interface EventsRemoteDataSource {
    suspend fun getEvents(countryCode: String): Result<List<Event>>
}
