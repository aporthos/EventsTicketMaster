package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.common.UseCase
import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CompleteOnboardingUseCase
    @Inject
    constructor(
        private val repository: DataStoreRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : UseCase<Unit, Unit>(dispatcher) {
        override suspend fun execute(params: Unit) = repository.completeOnboarding()
    }
