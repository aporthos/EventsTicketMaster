package com.globant.ticketmaster.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.globant.ticketmaster.core.database.converters.EventTypeConverter
import com.globant.ticketmaster.core.database.daos.ClassificationsDao
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.FavoritesEventsDao
import com.globant.ticketmaster.core.database.daos.LastVisitedEventsDao
import com.globant.ticketmaster.core.database.daos.SuggestionsEventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.models.entity.ClassificationsEntity
import com.globant.ticketmaster.core.models.entity.EventEntity
import com.globant.ticketmaster.core.models.entity.FavoritesEventEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedEventEntity
import com.globant.ticketmaster.core.models.entity.SuggestionEventEntity
import com.globant.ticketmaster.core.models.entity.VenuesEntity

@Database(
    entities = [
        EventEntity::class,
        VenuesEntity::class,
        ClassificationsEntity::class,
        SuggestionEventEntity::class,
        LastVisitedEventEntity::class,
        FavoritesEventEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    value = [
        EventTypeConverter::class,
    ],
)
abstract class TicketMasterDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao

    abstract fun classificationsDao(): ClassificationsDao

    abstract fun venuesDao(): VenuesDao

    abstract fun suggestionsEventsDao(): SuggestionsEventsDao

    abstract fun lastVisitedEventsDao(): LastVisitedEventsDao

    abstract fun favoritesEventsDao(): FavoritesEventsDao
}
