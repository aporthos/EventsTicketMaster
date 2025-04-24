package com.globant.ticketmaster.feature.favorites

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.globant.ticketmaster.core.common.COUNTRY_MX
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetCountriesUseCase
import com.globant.ticketmaster.core.domain.usecases.GetFavoritesEventsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateCountryUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.models.domain.CountryEvent
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.ui.CountryEventUi
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import com.globant.ticketmaster.core.models.ui.domainToUis
import com.globant.ticketmaster.core.models.ui.uiToDomain
import com.globant.ticketmaster.core.ui.BaseViewModel
import com.globant.ticketmaster.feature.countries.CountriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        getFavoritesEventsUseCase: GetFavoritesEventsUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
        getCountriesUseCase: GetCountriesUseCase,
        private val updateCountryUseCase: UpdateCountryUseCase,
    ) : BaseViewModel<FavoritesEvents, FavoritesEffects>() {
        private val countryCode = MutableStateFlow(COUNTRY_MX)
        private val search = MutableStateFlow("")
        private val countriesLocal = MutableStateFlow<List<CountryEvent>>(emptyList())

        init {
            viewModelScope.launch {
                getCountriesUseCase(Unit).collect { countries ->
                    countriesLocal.update { countries }
                    if (countries.isNotEmpty()) {
                        countryCode.update { countries.first { it.isSelected }.countryCode }
                    }
                }
            }
        }

        val uiState: Flow<PagingData<EventUi>> =
            countryCode
                .flatMapLatest { country ->
                    search
                        .debounce(500)
                        .flatMapLatest { search ->
                            getFavoritesEventsUseCase(GetFavoritesEventsUseCase.Params(search, country))
                        }.debounce(500)
                        .map { paging ->
                            paging.map(Event::domainToUis)
                        }
                }.cachedIn(viewModelScope)

        val countriesState: StateFlow<CountriesUiState> =
            countriesLocal
                .map { countries ->
                    if (countries.isEmpty()) {
                        CountriesUiState.Loading
                    } else {
                        CountriesUiState.Success(
                            current =
                                countries.find { it.isSelected }?.domainToUi() ?: countries
                                    .first()
                                    .domainToUi(),
                            countries = countries.domainToUis(),
                        )
                    }
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = CountriesUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )

        override fun onTriggerEvent(event: FavoritesEvents) {
            when (event) {
                is FavoritesEvents.OnUpdateFavoriteEvent -> updateFavoriteEvent(event.event)
                is FavoritesEvents.OnSelectCountry -> onSelectCountry(event.country)
                is FavoritesEvents.OnSearch -> onSearch(event.search)
                is FavoritesEvents.NavigateToDetailEvent ->
                    setEffect {
                        FavoritesEffects.NavigateToDetailEvent(
                            event.event,
                        )
                    }
            }
        }

        private fun onSearch(search: String) {
            this.search.value = search
        }

        private fun onSelectCountry(country: CountryEventUi) {
            viewModelScope.launch {
                updateCountryUseCase(UpdateCountryUseCase.Params(country.uiToDomain()))
            }
        }

        private fun updateFavoriteEvent(event: EventUi) {
            viewModelScope.launch {
                updateFavoriteEventUseCase(
                    UpdateFavoriteEventUseCase.Params(
                        event.idEvent,
                        EventType.Default,
                    ),
                ).onFailure {
                    Timber.e("updateFavoriteEvent -> $it")
                }
            }
        }
    }
