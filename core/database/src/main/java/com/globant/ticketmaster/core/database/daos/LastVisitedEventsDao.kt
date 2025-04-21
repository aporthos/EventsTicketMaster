package com.globant.ticketmaster.core.database.daos

import androidx.paging.PagingSource
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
            INNER JOIN events ON lastVisitedEvents.idLastVisitedEvent = events.idEvent
            WHERE events.countryCode = :countryCode
            ORDER BY lastVisitedEvents.cratedAt DESC
            LIMIT 5
    """,
    )
    fun getLastVisitedEvents(countryCode: String): Flow<List<LastVisitedWithEventsEntity>>

    @Transaction
    @Query(
        value = """
            SELECT * FROM lastVisitedEvents
            INNER JOIN events ON lastVisitedEvents.idLastVisitedEvent = events.idEvent
            WHERE events.countryCode = :countryCode
            AND events.name LIKE '%' || :keyword || '%'
            ORDER BY cratedAt DESC
    """,
    )
    fun getLastVisitedEventsPaging(
        keyword: String,
        countryCode: String,
    ): PagingSource<Int, LastVisitedWithEventsEntity>

    @Query(
        value = """
            UPDATE lastVisitedEvents SET cratedAt = :lastVisited 
            WHERE idLastVisitedEvent = :idEvent
    """,
    )
    fun updateLastVisitedEvent(
        idEvent: String,
        lastVisited: Long,
    ): Int
}
