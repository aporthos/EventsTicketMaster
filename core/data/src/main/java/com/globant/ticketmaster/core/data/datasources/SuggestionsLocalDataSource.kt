package com.globant.ticketmaster.core.data.datasources

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.database.daos.SuggestionsEventsDao
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.entity.SuggestionEventEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SuggestionsLocalDataSourceImpl
    @Inject
    constructor(
        private val suggestionsDao: SuggestionsEventsDao,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : SuggestionsLocalDataSource {
        override suspend fun addSuggestionsEvents(events: List<Event>) {
            withContext(dispatcher) {
                val suggestions = events.map { SuggestionEventEntity(it.idEvent) }
                suggestionsDao.insertOrIgnore(suggestions)
            }
        }

        override fun getIdsSuggestionsEvents(): Flow<List<String>> = suggestionsDao.getIdsSuggestionsEvents().flowOn(dispatcher)
    }

interface SuggestionsLocalDataSource {
    suspend fun addSuggestionsEvents(events: List<Event>)

    fun getIdsSuggestionsEvents(): Flow<List<String>>
}
