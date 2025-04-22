package com.globant.ticketmaster.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.models.entity.VenuesEntity

@Dao
interface VenuesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: List<VenuesEntity>)

    @Transaction
    @Query(
        value = """
        DELETE FROM venues WHERE idEventVenues IN (:idEvent)
    """,
    )
    suspend fun deleteVenuesByIdEvent(idEvent: List<String>)
}
