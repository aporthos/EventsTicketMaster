package com.globant.ticketmaster.core.data.datasources.suggestions

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.mappers.networkToDomains
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SuggestionsRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiServices: ApiServices,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : SuggestionsRemoteDataSource {
        override suspend fun getSuggestionsEvents(countryCode: String): Result<List<Event>> =
            withContext(dispatcher) {
                try {
                    val result = apiServices.getSuggestions(countryCode)
                    Result.success(
                        result.embedded?.events?.networkToDomains(countryCode = countryCode, page = 0)
                            ?: emptyList(),
                    )
                } catch (e: Exception) {
                    Timber.e("getSuggestions -> $e")
                    Result.failure(e)
                }
            }
    }

interface SuggestionsRemoteDataSource {
    suspend fun getSuggestionsEvents(countryCode: String): Result<List<Event>>
}
