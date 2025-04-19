package com.globant.ticketmaster.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.models.entity.EventEntity
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: List<EventEntity>)

    @Transaction
    @Query(
        value = """
            SELECT * FROM events
    """,
    )
    fun getAllEvents(): Flow<List<EventsWithVenuesEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM events
            WHERE idEvent IN (:ids)
    """,
    )
    fun getSuggestedEvents(ids: List<String>): Flow<List<EventsWithVenuesEntity>>
}
