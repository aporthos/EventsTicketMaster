package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetEventsUseCase.Params, List<Event>>(dispatcher) {
        data class Params(
            val countryCode: String,
            val keyword: String,
            val idClassification: String,
        )

        override fun execute(params: Params): Flow<List<Event>> =
            repository.getEvents(
                countryCode = params.countryCode,
                keyword = params.keyword,
                idClassification = params.idClassification,
            )
    }
