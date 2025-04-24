package com.globant.ticketmaster.core.testing

import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeSuggestionsRepository : SuggestionsRepository {
    private val events: MutableSharedFlow<Result<List<Event>>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getSuggestionsStream(countryCode: String): Flow<Result<List<Event>>> = events

    override suspend fun refreshSuggestions(countryCode: String): Result<List<Event>> {
        TODO("Not yet implemented")
    }

    fun addEvent(event: Result<List<Event>>) {
        events.tryEmit(event)
    }
}
