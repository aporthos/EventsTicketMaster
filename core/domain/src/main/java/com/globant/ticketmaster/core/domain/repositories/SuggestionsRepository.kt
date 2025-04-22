package com.globant.ticketmaster.core.domain.repositories

import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.flow.Flow

interface SuggestionsRepository {
    fun getSuggestions(countryCode: String): Flow<Result<List<Event>>>
}
