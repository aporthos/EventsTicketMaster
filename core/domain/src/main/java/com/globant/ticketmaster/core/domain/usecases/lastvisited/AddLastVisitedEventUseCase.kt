package com.globant.ticketmaster.core.domain.usecases.lastvisited

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.common.UseCase
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddLastVisitedEventUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : UseCase<AddLastVisitedEventUseCase.Params, Result<Boolean>>(dispatcher) {
        data class Params(
            val idEvent: String,
            val lastVisited: Long,
            val countryCode: String,
        )

        override suspend fun execute(params: Params): Result<Boolean> {
            val result =
                repository.setLastVisitedEvent(
                    idEvent = params.idEvent,
                    lastVisited = params.lastVisited,
                    countryCode = params.countryCode,
                )

            return if (result) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error update events last visited"))
            }
        }
    }
