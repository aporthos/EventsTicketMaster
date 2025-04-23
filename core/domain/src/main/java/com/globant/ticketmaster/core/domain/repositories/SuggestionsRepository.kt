package com.globant.ticketmaster.core.domain.repositories

import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.flow.Flow

interface SuggestionsRepository {
    fun getSuggestionsStream(countryCode: String): Flow<Result<List<Event>>>

    suspend fun refreshSuggestions(countryCode: String): Result<List<Event>>
}
