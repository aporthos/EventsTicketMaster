package com.globant.ticketmaster.core.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.common.EventType
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
            WHERE countryCode = :countryCode 
            AND idClassification LIKE :idClassification
            AND name LIKE :keyword
            LIMIT 20
    """,
    )
    fun getAllEvents(
        countryCode: String,
        keyword: String,
        idClassification: String,
    ): Flow<List<EventsWithVenuesEntity>>

    @Transaction
    @Query(
        value = """
        SELECT * FROM events
            WHERE countryCode = :countryCode 
            AND idClassification LIKE :idClassification
            AND name LIKE :keyword 
            ORDER BY createdAt ASC
    """,
    )
    fun pagingAllEvents(
        countryCode: String,
        keyword: String,
        idClassification: String,
    ): PagingSource<Int, EventEntity>

    @Transaction
    @Query(
        value = """
            SELECT * FROM events
            WHERE idEvent = :idEvent
    """,
    )
    fun getEventById(idEvent: String): Flow<EventsWithVenuesEntity>

    @Transaction
    @Query(
        value = """
            SELECT * FROM events
            WHERE eventType = :eventType
    """,
    )
    fun getFavoritesEvents(eventType: EventType): Flow<List<EventsWithVenuesEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM events
            WHERE idEvent IN (:ids)
    """,
    )
    fun getSuggestedEvents(ids: List<String>): Flow<List<EventsWithVenuesEntity>>

    @Query(
        value = """
            UPDATE events SET eventType = :eventType 
            WHERE idEvent = :idEvent
    """,
    )
    suspend fun updateFavoriteByIdEvent(
        idEvent: String,
        eventType: EventType,
    ): Int

    @Transaction
    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()
}
