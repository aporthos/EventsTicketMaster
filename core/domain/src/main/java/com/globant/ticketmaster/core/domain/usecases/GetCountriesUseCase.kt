package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import com.globant.ticketmaster.core.models.domain.CountryEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountriesUseCase
    @Inject
    constructor(
        private val repository: DataStoreRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<Unit, List<CountryEvent>>(dispatcher) {
        override fun execute(params: Unit): Flow<List<CountryEvent>> = repository.getCountries
    }
