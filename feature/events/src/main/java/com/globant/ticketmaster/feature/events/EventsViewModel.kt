package com.globant.ticketmaster.feature.events

import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.common.COUNTRY_MX
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetCountriesUseCase
import com.globant.ticketmaster.core.domain.usecases.GetSuggestionsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateCountryUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.RefreshSuggestionsUseCase
import com.globant.ticketmaster.core.models.domain.CountryEvent
import com.globant.ticketmaster.core.models.ui.CountryEventUi
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import com.globant.ticketmaster.core.models.ui.domainToUis
import com.globant.ticketmaster.core.models.ui.uiToDomain
import com.globant.ticketmaster.core.ui.BaseViewModel
import com.globant.ticketmaster.feature.countries.CountriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventsViewModel
    @Inject
    constructor(
        getSuggestionsEventsUseCase: GetSuggestionsUseCase,
        getClassificationsUseCase: GetClassificationsUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
        private val refreshSuggestionsUseCase: RefreshSuggestionsUseCase,
        getCountriesUseCase: GetCountriesUseCase,
        private val updateCountryUseCase: UpdateCountryUseCase,
        private val eventsResourcesManager: EventsResourcesManager,
    ) : BaseViewModel<EventsUiEvents, EventsEffects>() {
        private val isRefreshing = MutableStateFlow(false)
        private val countryCode = MutableStateFlow(COUNTRY_MX)
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

        val suggestionsEventsState: StateFlow<EventsUiState> =
            countryCode
                .flatMapLatest { country ->
                    isRefreshing
                        .combine(
                            getSuggestionsEventsUseCase(
                                GetSuggestionsUseCase.Params(
                                    countryCode = country,
                                ),
                            ),
                        ) { isRefreshing, events ->
                            EventsUiState.Items(
                                isRefreshing = isRefreshing,
                                suggestionsEvents = events.suggestionsEvents.domainToUis(),
                                lastVisitedEvents = events.lastVisitedEvents.domainToUis(),
                            )
                        }
                }.distinctUntilChanged()
                .stateIn(
                    scope = viewModelScope,
                    initialValue = EventsUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )

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

        val classificationsState: StateFlow<ClassificationsUiState> =
            getClassificationsUseCase(Unit)
                .map(ClassificationsUiState::Items)
                .stateIn(
                    scope = viewModelScope,
                    initialValue = ClassificationsUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )

        override fun onTriggerEvent(event: EventsUiEvents) {
            when (event) {
                is EventsUiEvents.OnUpdateFavoriteEvent -> updateFavoriteEvent(event.event)
                is EventsUiEvents.NavigateToDetail ->
                    setEffect {
                        EventsEffects.NavigateToDetailEvent(event.event)
                    }

                EventsUiEvents.OnRefresh -> onRefresh()
                EventsUiEvents.NavigateToSearch -> setEffect { EventsEffects.NavigateToSearch }
                is EventsUiEvents.NavigateToClassification ->
                    setEffect { EventsEffects.NavigateToClassification(event.classification) }

                EventsUiEvents.NavigateToLastVisited ->
                    setEffect { EventsEffects.NavigateToLastVisited }

                is EventsUiEvents.OnSelectCountry -> onSelectCountry(event.country)
            }
        }

        private fun onSelectCountry(country: CountryEventUi) {
            viewModelScope.launch {
                updateCountryUseCase(UpdateCountryUseCase.Params(country.uiToDomain()))
            }
        }

        private fun updateFavoriteEvent(event: EventUi) {
            val eventType =
                if (event.eventType == EventType.Default) {
                    EventType.Favorite
                } else {
                    EventType.Default
                }
            viewModelScope.launch {
                updateFavoriteEventUseCase(
                    UpdateFavoriteEventUseCase.Params(
                        event.idEvent,
                        eventType,
                    ),
                ).onFailure {
                    Timber.e("updateFavoriteEvent -> $it")
                }
            }
        }

        private fun onRefresh() {
            viewModelScope.launch {
                isRefreshing.update { true }
                refreshSuggestionsUseCase(
                    RefreshSuggestionsUseCase.Params(
                        countryCode = countryCode.value,
                    ),
                ).onSuccess {
                    setEffect { EventsEffects.Success(eventsResourcesManager.successMessage) }
                }.onFailure {
                    setEffect { EventsEffects.ShowError(eventsResourcesManager.errorMessage) }
                }
                delay(100)
                isRefreshing.update { false }
            }
        }
    }
