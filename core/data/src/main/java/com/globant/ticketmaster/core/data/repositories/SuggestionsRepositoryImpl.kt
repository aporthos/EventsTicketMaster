package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsRemoteDataSource
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
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : SuggestionsRepository {
        override fun getSuggestions(countryCode: String): Flow<List<Event>> =
            flow {
                val suggestedEvents =
                    localSuggestions.getIdsSuggestionsEvents().flatMapLatest { ids ->
                        localEvents.getSuggestedEvents(ids)
                    }

//                if (suggestedEvents.first().isEmpty()) {
                val result = remote.getSuggestionsEvents(countryCode)
                result
                    .onSuccess {
                        localEvents.addEvents(it)
                        localSuggestions.addSuggestionsEvents(it)
                    }.onFailure {
                        Timber.e("getSuggestions -> $it")
                    }
//                }
                emitAll(suggestedEvents)
            }.flowOn(dispatcher)
    }
