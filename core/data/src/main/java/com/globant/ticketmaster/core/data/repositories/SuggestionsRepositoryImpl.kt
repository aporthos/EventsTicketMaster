package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.datasources.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.SuggestionsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.SuggestionsRemoteDataSource
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class SuggestionsRepositoryImpl
    @Inject
    constructor(
        private val remote: SuggestionsRemoteDataSource,
        private val localEvents: EventsLocalDataSource,
        private val localSuggestions: SuggestionsLocalDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : SuggestionsRepository {
        override fun getSuggestions(countryCode: String): Flow<List<Event>> =
            flow {
                val localEvents =
                    localSuggestions.getIdsSuggestionsEvents().flatMapLatest { ids ->
                        localEvents.getSuggestedEvents(ids)
                    }
                val result = remote.getSuggestionsEvents(countryCode)
                result
                    .onSuccess {
                        localSuggestions.addSuggestionsEvents(it)
                    }.onFailure {
                        Timber.e("getSuggestions -> $it")
                    }
                emitAll(localEvents)
            }.flowOn(ioDispatcher)
    }
