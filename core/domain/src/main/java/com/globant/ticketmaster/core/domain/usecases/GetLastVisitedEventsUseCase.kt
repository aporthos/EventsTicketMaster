package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastVisitedEventsUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetLastVisitedEventsUseCase.Params, List<Event>>(dispatcher) {
        data class Params(
            val countryCode: String,
        )

        override fun execute(params: Params): Flow<List<Event>> = repository.getLastVisitedEvents(countryCode = params.countryCode)
    }
