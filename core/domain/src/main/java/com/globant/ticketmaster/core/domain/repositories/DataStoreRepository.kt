package com.globant.ticketmaster.core.domain.repositories

import com.globant.ticketmaster.core.models.domain.CountryEvent
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun completeOnboarding()

    val canShowOnboarding: Flow<Boolean>

    val getCountries: Flow<List<CountryEvent>>
    val countryCode: String

    suspend fun saveCountries(countries: List<CountryEvent>)

    suspend fun updateCountry(country: CountryEvent)
}
