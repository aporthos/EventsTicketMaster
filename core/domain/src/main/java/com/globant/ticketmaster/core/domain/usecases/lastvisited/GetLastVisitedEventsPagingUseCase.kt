package com.globant.ticketmaster.core.domain.usecases.lastvisited

import androidx.paging.PagingData
import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastVisitedEventsPagingUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetLastVisitedEventsPagingUseCase.Params, PagingData<Event>>(dispatcher) {
        data class Params(
            val keyword: String,
            val countryCode: String,
        )

        override fun execute(params: Params): Flow<PagingData<Event>> =
            repository.getLastVisitedEventsPaging(
                countryCode = params.countryCode,
                keyword = params.keyword,
            )
    }
