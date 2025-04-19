package com.globant.ticketmaster.core.domain.repositories

import com.globant.ticketmaster.core.models.domain.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
    fun getEvents(countryCode: String): Flow<List<Event>>
}
