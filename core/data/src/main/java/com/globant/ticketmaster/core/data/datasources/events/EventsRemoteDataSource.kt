package com.globant.ticketmaster.core.data.datasources.events

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
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : EventsRemoteDataSource {
        override suspend fun getEvents(
            countryCode: String,
            keyword: String,
            page: Int,
            idClassification: String,
        ): Result<List<Event>> =
            withContext(dispatcher) {
                try {
                    val result =
                        apiServices.getEvents(
                            countryCode = countryCode,
                            keyword = keyword,
                            page = page,
                            idClassification = idClassification,
                        )
                    Result.success(
                        result.embedded?.events?.networkToDomains(
                            countryCode = countryCode,
                            page = page,
                        )
                            ?: emptyList(),
                    )
                } catch (e: Exception) {
                    Timber.e("getEvents -> $e")
                    Result.failure(e)
                }
            }
    }

interface EventsRemoteDataSource {
    suspend fun getEvents(
        countryCode: String,
        keyword: String,
        page: Int,
        idClassification: String,
    ): Result<List<Event>>
}
