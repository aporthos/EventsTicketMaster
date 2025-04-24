package com.globant.ticketmaster.core.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.globant.ticketmaster.core.data.di.DataModule
import com.globant.ticketmaster.core.domain.repositories.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class DataStoreRepositoryImpl
    @Inject
    constructor(
        @Named(DataModule.USER_DATASTORE) private val dataStore: DataStore<Preferences>,
    ) : DataStoreRepository {
        companion object {
            private val KEY_SHOW_ONBOARDING = booleanPreferencesKey("SHOW_ONBOARDING")
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
    }
