package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSuggestionsUseCase
    @Inject
    constructor(
        private val suggestionsRepository: SuggestionsRepository,
        private val eventsRepository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetSuggestionsUseCase.Params, HomeResult>(dispatcher) {
        data class Params(
            val countryCode: String,
        )

        override fun execute(params: Params): Flow<HomeResult> =
            combine(
                suggestionsRepository.getSuggestionsStream(params.countryCode),
                eventsRepository.getLastVisitedEvents(params.countryCode),
            ) { suggestions, events ->
                HomeResult(
                    suggestionsEvents = suggestions.getOrElse { emptyList() },
                    lastVisitedEvents = events,
                )
            }
    }

data class HomeResult(
    val suggestionsEvents: List<Event>,
    val lastVisitedEvents: List<Event>,
)
