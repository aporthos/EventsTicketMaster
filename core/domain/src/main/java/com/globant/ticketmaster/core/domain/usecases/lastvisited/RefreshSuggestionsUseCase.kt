package com.globant.ticketmaster.core.domain.usecases.lastvisited

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.common.UseCase
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RefreshSuggestionsUseCase
    @Inject
    constructor(
        private val suggestionsRepository: SuggestionsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : UseCase<RefreshSuggestionsUseCase.Params, Result<List<Event>>>(dispatcher) {
        data class Params(
            val countryCode: String,
        )

        override suspend fun execute(params: Params): Result<List<Event>> = suggestionsRepository.refreshSuggestions(params.countryCode)
    }
