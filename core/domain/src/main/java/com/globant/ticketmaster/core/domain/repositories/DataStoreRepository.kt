package com.globant.ticketmaster.core.domain.repositories

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun completeOnboarding()

    val canShowOnboarding: Flow<Boolean>
}
