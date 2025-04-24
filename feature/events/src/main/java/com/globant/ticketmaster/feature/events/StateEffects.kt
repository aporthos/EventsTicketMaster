package com.globant.ticketmaster.feature.events

import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.ui.CountryEventUi
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.ViewEffect
import com.globant.ticketmaster.core.ui.ViewEvent

sealed interface EventsUiState {
    data class Items(
        val isRefreshing: Boolean,
        val suggestionsEvents: List<EventUi>,
        val lastVisitedEvents: List<EventUi>,
    ) : EventsUiState

    data object Error : EventsUiState

    data object Loading : EventsUiState
}

sealed interface EventsUiEvents : ViewEvent {
    data class OnUpdateFavoriteEvent(
        val event: EventUi,
    ) : EventsUiEvents

    data class NavigateToDetail(
        val event: EventUi,
    ) : EventsUiEvents

    data object NavigateToSearch : EventsUiEvents

    data object OnRefresh : EventsUiEvents

    data class NavigateToClassification(
        val classification: Classification,
    ) : EventsUiEvents

    data object NavigateToLastVisited : EventsUiEvents

    data class OnSelectCountry(
        val country: CountryEventUi,
    ) : EventsUiEvents

    data object OnRetry : EventsUiEvents
}

sealed interface EventsEffects : ViewEffect {
    data class Success(
        val message: String,
    ) : EventsEffects

    data class ShowError(
        val message: String,
    ) : EventsEffects

    data class NavigateToDetailEvent(
        val event: EventUi,
    ) : EventsEffects

    data object NavigateToSearch : EventsEffects

    data class NavigateToClassification(
        val classification: Classification,
    ) : EventsEffects

    data object NavigateToLastVisited : EventsEffects
}

sealed interface ClassificationsUiState {
    data class Items(
        val classifications: List<Classification>,
    ) : ClassificationsUiState

    data object Loading : ClassificationsUiState
}
