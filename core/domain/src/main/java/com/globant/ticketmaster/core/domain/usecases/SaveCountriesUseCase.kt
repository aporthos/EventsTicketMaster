package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.common.UseCase
import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import com.globant.ticketmaster.core.models.domain.CountryEvent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveCountriesUseCase
    @Inject
    constructor(
        private val repository: DataStoreRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : UseCase<SaveCountriesUseCase.Params, Unit>(dispatcher) {
        data class Params(
            val countries: List<CountryEvent>,
        )

        override suspend fun execute(params: Params) {
            repository.saveCountries(params.countries)
        }
    }
