package com.globant.ticketmaster.core.domain.usecases

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.common.UseCase
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateFavoriteEventUseCase
    @Inject
    constructor(
        private val repository: EventsRepository,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : UseCase<UpdateFavoriteEventUseCase.Params, Result<Boolean>>(dispatcher) {
        data class Params(
            val idEvent: String,
            val eventType: EventType,
        )

        override suspend fun execute(params: Params): Result<Boolean> {
            val result =
                repository.setFavoriteEvent(idEvent = params.idEvent, eventType = params.eventType)

            return if (result) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error update events favorites"))
            }
        }
    }
