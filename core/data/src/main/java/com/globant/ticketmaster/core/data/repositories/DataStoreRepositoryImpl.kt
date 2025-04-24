package com.globant.ticketmaster.core.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.globant.ticketmaster.core.common.COUNTRY_MX
import com.globant.ticketmaster.core.data.di.DataModule
import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import com.globant.ticketmaster.core.models.domain.CountryEvent
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class DataStoreRepositoryImpl
    @Inject
    constructor(
        @Named(DataModule.USER_DATASTORE) private val dataStore: DataStore<Preferences>,
        private val adapter: JsonAdapter<List<CountryEvent>>,
    ) : DataStoreRepository {
        companion object {
            private val KEY_SHOW_ONBOARDING = booleanPreferencesKey("showOnboarding")
            private val KEY_COUNTRIES = stringPreferencesKey("countries")
        }

        override suspend fun completeOnboarding() {
            dataStore.edit { preferences ->
                preferences[KEY_SHOW_ONBOARDING] = false
            }
        }

        override val canShowOnboarding: Flow<Boolean>
            get() =
                dataStore.data.map { preferences ->
                    preferences[KEY_SHOW_ONBOARDING] ?: true
                }
        override val getCountries: Flow<List<CountryEvent>>
            get() {

                return dataStore.data
                    .distinctUntilChanged()
                    .map { preferences ->
                        val countriesString = preferences[KEY_COUNTRIES]
                        if (countriesString.isNullOrEmpty()) {
                            return@map emptyList<CountryEvent>()
                        }
                        adapter.fromJson(countriesString) ?: emptyList()
                    }.distinctUntilChanged()
            }
        override val countryCode: String
            get() {
                return runBlocking {
                    val countries = dataStore.data.first()[KEY_COUNTRIES]

                    if (countries.isNullOrEmpty()) {
                        return@runBlocking COUNTRY_MX
                    }
                    val countriesSaved = adapter.fromJson(countries) ?: emptyList()
                    countriesSaved.find { it.isSelected }?.countryCode ?: COUNTRY_MX
                }
            }

        override suspend fun saveCountries(countries: List<CountryEvent>) {
            val countriesJson = adapter.toJson(countries)
            dataStore.edit { preferences ->
                preferences[KEY_COUNTRIES] = countriesJson
            }
        }

        override suspend fun updateCountry(country: CountryEvent) {
            val countriesString = dataStore.data.first()[KEY_COUNTRIES]
            if (countriesString.isNullOrEmpty()) {
                return
            }
            val countriesList = adapter.fromJson(countriesString) ?: emptyList()
            val countries =
                countriesList.map {
                    it.copy(isSelected = it.countryCode == country.countryCode)
                }
            saveCountries(countries)
        }
    }
