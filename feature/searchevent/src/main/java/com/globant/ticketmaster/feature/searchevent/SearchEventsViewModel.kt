package com.globant.ticketmaster.feature.searchevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetEventsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchEventsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        getEventsUseCase: GetEventsUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
    ) : ViewModel() {
        private val idClassification = savedStateHandle.toRoute<SearchEvents>().idClassification
        private val keyword = MutableStateFlow("")

        val eventsState: StateFlow<EventsUiState> =
            keyword
                .debounce(500)
                .flatMapLatest { keyword ->
                    getEventsUseCase(
                        GetEventsUseCase.Params(
                            countryCode = "MX",
                            keyword = keyword,
                            idClassification = idClassification,
                        ),
                    )
                }.map { result ->
                    if (result.isEmpty()) {
                        EventsUiState.Empty
                    } else {
                        EventsUiState.Success(
                            events = result.domainToUis(),
                        )
                    }
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = EventsUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )

        fun onSearch(search: String) {
            this.keyword.value = search
        }

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
