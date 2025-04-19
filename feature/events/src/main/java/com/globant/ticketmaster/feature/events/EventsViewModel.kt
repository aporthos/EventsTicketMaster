package com.globant.ticketmaster.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetEventsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetSuggestionsUseCase
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
class EventsViewModel
    @Inject
    constructor(
        private val getEventsUseCase: GetEventsUseCase,
        getSuggestionsEventsUseCase: GetSuggestionsUseCase,
        getClassificationsUseCase: GetClassificationsUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
    ) : ViewModel() {
        init {
            viewModelScope.launch {
                getEventsUseCase(GetEventsUseCase.Params("MX")).collect {
                    Timber.d("getEventsUseCase -> $it")
                }
            }
        }

        val suggestionsEventsState: StateFlow<EventsUiState> =
            getSuggestionsEventsUseCase(GetSuggestionsUseCase.Params("MX"))
                .map { result ->
                    EventsUiState.Items(
                        suggestionsEvents = result.domainToUis(),
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

        fun updateFavoriteEvent(event: EventUi) {
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
    }
