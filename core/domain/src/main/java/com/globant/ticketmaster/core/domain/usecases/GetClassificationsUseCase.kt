package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.FlowSingleUseCase
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClassificationsUseCase
    @Inject
    constructor(
        private val repository: ClassificationsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : FlowSingleUseCase<Unit, Result<List<Classification>>>(dispatcher) {
        override fun execute(params: Unit): Flow<Result<List<Classification>>> = repository.getClassifications()
    }
