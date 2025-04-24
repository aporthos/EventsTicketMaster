package com.globant.ticketmaster.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.paging.PagingConfig
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsLocalDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsRemoteDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.events.EventsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsRemoteDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsLocalDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsRemoteDataSourceImpl
import com.globant.ticketmaster.core.data.repositories.ClassificationsRepositoryImpl
import com.globant.ticketmaster.core.data.repositories.DataStoreRepositoryImpl
import com.globant.ticketmaster.core.data.repositories.EventsRepositoryImpl
import com.globant.ticketmaster.core.data.repositories.SuggestionsRepositoryImpl
import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 1
        const val USER_DATASTORE = "userDataStore"

        @Singleton
        @Provides
        fun providesService(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)

        @Singleton
        @Provides
        fun providesPagerConfig(): PagingConfig =
            PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
            )

        @Named(USER_DATASTORE)
        @Singleton
        @Provides
        fun providesUserDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> =
            PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile(USER_DATASTORE)
            }
    }

    @Binds
    fun bindsEventsRepository(repository: EventsRepositoryImpl): EventsRepository

    @Binds
    fun bindsEventsRemoteDataSource(dataSource: EventsRemoteDataSourceImpl): EventsRemoteDataSource

    @Binds
    fun bindsEventsLocalDataSource(dataSource: EventsLocalDataSourceImpl): EventsLocalDataSource

    @Binds
    fun bindsClassificationsRemoteDataSource(dataSource: ClassificationsRemoteDataSourceImpl): ClassificationsRemoteDataSource

    @Binds
    fun bindsClassificationsLocalDataSource(dataSource: ClassificationsLocalDataSourceImpl): ClassificationsLocalDataSource

    @Binds
    fun bindsClassificationsRepository(repository: ClassificationsRepositoryImpl): ClassificationsRepository

    @Binds
    fun bindsSuggestionsRemoteDataSource(dataSource: SuggestionsRemoteDataSourceImpl): SuggestionsRemoteDataSource

    @Binds
    fun bindsSuggestionsLocalDataSource(dataSource: SuggestionsLocalDataSourceImpl): SuggestionsLocalDataSource

    @Binds
    fun bindsSuggestionsRepository(repository: SuggestionsRepositoryImpl): SuggestionsRepository

    @Binds
    fun bindsDataStoreRepository(repository: DataStoreRepositoryImpl): DataStoreRepository
}
