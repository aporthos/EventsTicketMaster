package com.globant.ticketmaster.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetFavoritesEventsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        getFavoritesEventsUseCase: GetFavoritesEventsUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
    ) : ViewModel() {
        val uiState: StateFlow<EventsUiState> =
            getFavoritesEventsUseCase(GetFavoritesEventsUseCase.Params("MX"))
                .map { result ->
                    if (result.domainToUis().isEmpty()) {
                        EventsUiState.Empty
                    } else {
                        EventsUiState.Favorites(
                            events = result.domainToUis(),
                        )
                    }
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = EventsUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )

        fun updateFavoriteEvent(event: EventUi) {
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
