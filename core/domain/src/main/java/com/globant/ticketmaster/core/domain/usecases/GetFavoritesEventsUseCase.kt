package com.globant.ticketmaster.core.domain.usecases

import androidx.paging.PagingData
import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesEventsUseCase
    @Inject
    constructor(
        private val eventsRepository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetFavoritesEventsUseCase.Params, PagingData<Event>>(dispatcher) {
        data class Params(
            val search: String,
            val countryCode: String,
        )

        override fun execute(params: Params): Flow<PagingData<Event>> =
            eventsRepository.getFavoritesEventsPaging(params.search, params.countryCode)
    }
