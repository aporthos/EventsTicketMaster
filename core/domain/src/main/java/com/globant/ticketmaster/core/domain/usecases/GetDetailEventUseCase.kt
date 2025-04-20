package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailEventUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetDetailEventUseCase.Params, Event>(dispatcher) {
        data class Params(
            val idEvent: String,
        )

        override fun execute(params: Params): Flow<Event> = repository.getDetailEvent(params.idEvent)
    }
