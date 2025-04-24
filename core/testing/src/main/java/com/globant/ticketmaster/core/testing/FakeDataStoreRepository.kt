package com.globant.ticketmaster.core.testing

import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import com.globant.ticketmaster.core.models.domain.CountryEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeDataStoreRepository : DataStoreRepository {
    private val countries: MutableSharedFlow<List<CountryEvent>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override suspend fun completeOnboarding() {
        TODO("Not yet implemented")
    }

    override val canShowOnboarding: Flow<Boolean>
        get() = TODO("Not yet implemented")
    override val getCountries: Flow<List<CountryEvent>> = countries
    override val countryCode: String
        get() = "Mx"

    override suspend fun saveCountries(countries: List<CountryEvent>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCountry(country: CountryEvent) {
        TODO("Not yet implemented")
    }
}
