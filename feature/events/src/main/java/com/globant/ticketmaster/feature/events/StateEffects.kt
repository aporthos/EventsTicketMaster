package com.globant.ticketmaster.feature.events

import com.globant.ticketmaster.core.models.domain.Event

sealed interface EventsUiState {
    data class Items(
        var default: List<Event>,
        var favorites: List<Event>,
    ) : EventsUiState

    data object Error : EventsUiState

    data object Loading : EventsUiState
}
