package com.globant.ticketmaster.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.models.entity.SuggestionEventEntity
import com.globant.ticketmaster.core.models.entity.SuggestionsWithEventsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionsEventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: List<SuggestionEventEntity>): List<Long>

    @Transaction
    @Query(
        value = """
            SELECT * FROM suggestionsEvents
            INNER JOIN events ON suggestionsEvents.idEvent = events.idEvent
            WHERE events.countryCode = :countryCode
            ORDER BY events.startEvent ASC
    """,
    )
    fun getSuggestionsEvents(countryCode: String): Flow<List<SuggestionsWithEventsEntity>>
}
