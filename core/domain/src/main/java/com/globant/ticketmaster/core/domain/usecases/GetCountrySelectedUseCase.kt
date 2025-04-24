package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import javax.inject.Inject

class GetCountrySelectedUseCase
    @Inject
    constructor(
        private val repository: DataStoreRepository,
    ) {
        operator fun invoke(): String = repository.countryCode
    }
