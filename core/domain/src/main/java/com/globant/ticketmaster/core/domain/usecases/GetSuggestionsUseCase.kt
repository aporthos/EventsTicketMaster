package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSuggestionsUseCase
    @Inject
    constructor(
        private val repository: SuggestionsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetSuggestionsUseCase.Params, List<Event>>(dispatcher) {
        data class Params(
            val countryCode: String,
        )

        override fun execute(params: Params): Flow<List<Event>> = repository.getSuggestions(params.countryCode)
    }
