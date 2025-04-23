package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsRemoteDataSource
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SuggestionsRepositoryImpl
    @Inject
    constructor(
        private val remote: SuggestionsRemoteDataSource,
        private val localEvents: EventsLocalDataSource,
        private val localSuggestions: SuggestionsLocalDataSource,
        private val classifications: ClassificationsLocalDataSource,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : SuggestionsRepository {
        override fun getSuggestionsStream(countryCode: String): Flow<Result<List<Event>>> =
            object : ManagerCacheNetwork<List<Event>>() {
                override suspend fun shouldFetch(data: List<Event>): Boolean = data.isEmpty()

                override fun fetchLocal(): Flow<List<Event>> = localSuggestions.getSuggestionsEvents(countryCode)

                override suspend fun fetchRemote(): Result<List<Event>> = callService(countryCode)

                override suspend fun saveResult(items: List<Event>) {
                    localEvents.addEvents(items)
                    localSuggestions.addSuggestionsEvents(items)
                }
            }.asFlow().flowOn(dispatcher)

        override suspend fun refreshSuggestions(countryCode: String): Result<List<Event>> =
            callService(countryCode).onSuccess {
                localEvents.addEvents(it)
                localSuggestions.addSuggestionsEvents(it)
            }

        private suspend fun callService(countryCode: String): Result<List<Event>> {
            val ids =
                classifications
                    .getClassificationsStream()
                    .first()
                    .joinToString { it.idClassification }
                    .replace(" ", "")
            return remote.getSuggestionsEvents(
                countryCode,
                ids,
            )
        }
    }
