package com.globant.ticketmaster.feature.searchevent

import com.globant.ticketmaster.core.models.ui.EventUi

sealed interface EventsUiState {
    data class Success(
        val events: List<EventUi>,
    ) : EventsUiState

    data object Empty : EventsUiState

    data object Loading : EventsUiState
}
