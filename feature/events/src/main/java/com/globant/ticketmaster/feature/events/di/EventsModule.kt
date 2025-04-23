package com.globant.ticketmaster.feature.events.di

import com.globant.ticketmaster.feature.events.EventsResourcesManager
import com.globant.ticketmaster.feature.events.EventsResourcesManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface EventsModule {
    @Binds
    fun bindEventsResourcesManager(eventsResourcesManagerImpl: EventsResourcesManagerImpl): EventsResourcesManager
}
