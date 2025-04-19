package com.globant.ticketmaster.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.models.entity.SuggestionEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SuggestionsEventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: List<SuggestionEventEntity>)

    @Transaction
    @Query(
        value = """
            SELECT idEvent FROM suggestionsEvents
    """,
    )
    fun getIdsSuggestionsEvents(): Flow<List<String>>
}
