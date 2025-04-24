package com.globant.ticketmaster.core.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.globant.ticketmaster.core.models.entity.ClassificationsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassificationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrIgnore(entities: List<ClassificationsEntity>): List<Long>

    @Transaction
    @Query(
        value = """
            SELECT * FROM classifications
    """,
    )
    fun getAllClassifications(): Flow<List<ClassificationsEntity>>
}
