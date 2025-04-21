package com.globant.ticketmaster.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.models.entity.LastVisitedEventEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedWithEventsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LastVisitedEventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: LastVisitedEventEntity): Long

    @Transaction
    @Query(
        value = """
            SELECT * FROM lastVisitedEvents
            WHERE countryCode = :countryCode
            ORDER BY lastVisited DESC
            LIMIT 5
    """,
    )
    fun getLastVisitedEvents(countryCode: String): Flow<List<LastVisitedWithEventsEntity>>

    @Query(
        value = """
            UPDATE lastVisitedEvents SET lastVisited = :lastVisited 
            WHERE idLastVisitedEvent = :idEvent
    """,
    )
    fun updateLastVisitedEvent(
        idEvent: String,
        lastVisited: Long,
    ): Int
}
