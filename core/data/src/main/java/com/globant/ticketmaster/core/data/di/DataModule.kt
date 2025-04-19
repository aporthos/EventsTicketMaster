package com.globant.ticketmaster.core.data.di

import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.datasources.ClassificationsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.ClassificationsLocalDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.ClassificationsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.ClassificationsRemoteDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.EventsLocalDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.EventsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.EventsRemoteDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.SuggestionsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.SuggestionsLocalDataSourceImpl
import com.globant.ticketmaster.core.data.datasources.SuggestionsRemoteDataSource
import com.globant.ticketmaster.core.data.datasources.SuggestionsRemoteDataSourceImpl
import com.globant.ticketmaster.core.data.repositories.ClassificationsRepositoryImpl
import com.globant.ticketmaster.core.data.repositories.EventsRepositoryImpl
import com.globant.ticketmaster.core.data.repositories.SuggestionsRepositoryImpl
import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.domain.repositories.EventsRepository
import com.globant.ticketmaster.core.domain.repositories.SuggestionsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    companion object {
        @Singleton
        @Provides
        fun providesService(retrofit: Retrofit): ApiServices = retrofit.create(ApiServices::class.java)
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
}
