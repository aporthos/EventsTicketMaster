package com.globant.ticketmaster.core.database.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.entity.FavoritesEventEntity
import com.globant.ticketmaster.core.models.entity.FavoritesWithEventsEntity

@Dao
interface FavoritesEventsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: FavoritesEventEntity): Long

    @Transaction
    @Query(
        value = """
            SELECT * FROM favoritesEvents
            INNER JOIN events ON favoritesEvents.idFavoriteEvent = events.idEvent
            WHERE events.countryCode = :countryCode
            AND events.name LIKE '%' || :keyword || '%'
            and favoritesEvents.eventType = :eventType
            ORDER BY events.createdAt DESC
    """,
    )
    fun getFavoritesEventsPaging(
        keyword: String,
        countryCode: String,
        eventType: EventType = EventType.Favorite,
    ): PagingSource<Int, FavoritesWithEventsEntity>
}
