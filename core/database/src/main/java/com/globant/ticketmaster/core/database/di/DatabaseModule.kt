package com.globant.ticketmaster.core.database.di

import android.content.Context
import androidx.room.Room
import com.globant.ticketmaster.core.database.EventsTransactions
import com.globant.ticketmaster.core.database.EventsTransactionsImpl
import com.globant.ticketmaster.core.database.TicketMasterDatabase
import com.globant.ticketmaster.core.database.daos.ClassificationsDao
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.LastVisitedEventsDao
import com.globant.ticketmaster.core.database.daos.SuggestionsEventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {
    companion object {
        private const val DB_NAME = "TicketMasterDatabase.db"

        @Provides
        @Singleton
        fun providesDatabase(
            @ApplicationContext context: Context,
        ): TicketMasterDatabase =
            Room
                .databaseBuilder(
                    context = context,
                    TicketMasterDatabase::class.java,
                    DB_NAME,
                ).fallbackToDestructiveMigration()
                .build()

        @Provides
        @Singleton
        fun providesEventsDao(database: TicketMasterDatabase): EventsDao = database.eventsDao()

        @Provides
        @Singleton
        fun providesClassificationsDao(database: TicketMasterDatabase): ClassificationsDao = database.classificationsDao()

        @Provides
        @Singleton
        fun providesVenuesDao(database: TicketMasterDatabase): VenuesDao = database.venuesDao()

        @Provides
        @Singleton
        fun providesSuggestionsEventsDao(database: TicketMasterDatabase): SuggestionsEventsDao = database.suggestionsEventsDao()

        @Provides
        @Singleton
        fun providesLastVisitedEventsDao(database: TicketMasterDatabase): LastVisitedEventsDao = database.lastVisitedEventsDao()
    }

    @Binds
    fun bindsEventsTransactions(eventsTransactionsImpl: EventsTransactionsImpl): EventsTransactions
}
