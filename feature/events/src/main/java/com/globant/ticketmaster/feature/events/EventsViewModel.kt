package com.globant.ticketmaster.feature.events

import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetSuggestionsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.RefreshSuggestionsUseCase
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUis
import com.globant.ticketmaster.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
        private val eventsResourcesManager: EventsResourcesManager,
    ) : BaseViewModel<EventsUiEvents, EventsEffects>() {
        private val isRefreshing = MutableStateFlow(false)

        val suggestionsEventsState: StateFlow<EventsUiState> =
            isRefreshing
                .combine(
                    getSuggestionsEventsUseCase(
                        GetSuggestionsUseCase.Params(
                            countryCode = "MX",
                        ),
                    ),
                ) { isRefreshing, events ->
                    EventsUiState.Items(
                        isRefreshing = isRefreshing,
                        suggestionsEvents = events.suggestionsEvents.domainToUis(),
                        lastVisitedEvents = events.lastVisitedEvents.domainToUis(),
                    )
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = EventsUiState.Loading,
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
                is EventsUiEvents.UpdateFavoriteEvent -> updateFavoriteEvent(event.event)
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
                        countryCode = "MX",
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
