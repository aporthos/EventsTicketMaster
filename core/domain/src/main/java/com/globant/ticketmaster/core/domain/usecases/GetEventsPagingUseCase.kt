package com.globant.ticketmaster.core.domain.usecases

import androidx.paging.PagingData
import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsPagingUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<GetEventsPagingUseCase.Params, PagingData<Event>>(dispatcher) {
        data class Params(
            val countryCode: String,
            val keyword: String,
            val idClassification: String,
        )

        override fun execute(params: Params): Flow<PagingData<Event>> =
            repository.getEventsPaging(
                countryCode = params.countryCode,
                keyword = params.keyword,
                idClassification = params.idClassification,
            )
    }
