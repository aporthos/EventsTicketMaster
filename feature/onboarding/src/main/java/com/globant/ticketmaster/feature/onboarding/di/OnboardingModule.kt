package com.globant.ticketmaster.feature.onboarding.di

import com.globant.ticketmaster.feature.onboarding.OnboardingResourcesManager
import com.globant.ticketmaster.feature.onboarding.OnboardingResourcesManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OnboardingModule {
    @Binds
    fun bindsResourcesManager(manager: OnboardingResourcesManagerImpl): OnboardingResourcesManager
}
