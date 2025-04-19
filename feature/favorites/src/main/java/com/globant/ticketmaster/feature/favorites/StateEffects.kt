package com.globant.ticketmaster.feature.favorites

import com.globant.ticketmaster.core.models.ui.EventUi

sealed interface EventsUiState {
    data class Favorites(
        val events: List<EventUi>,
    ) : EventsUiState

    data object Empty : EventsUiState

    data object Loading : EventsUiState
}
