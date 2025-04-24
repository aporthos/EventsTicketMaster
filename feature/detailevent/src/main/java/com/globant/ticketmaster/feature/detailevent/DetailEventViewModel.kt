package com.globant.ticketmaster.feature.detailevent

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetDetailEventUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.AddLastVisitedEventUseCase
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        getDetailEventUseCase: GetDetailEventUseCase,
        private val addLastVisitedEventUseCase: AddLastVisitedEventUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
    ) : ViewModel() {
        @VisibleForTesting
        val idEvent = savedStateHandle.toRoute<DetailEvent>().idEvent

        init {
            viewModelScope.launch {
                addLastVisitedEvent(idEvent)
            }
        }

        val eventState: StateFlow<EventUiState> =
            getDetailEventUseCase(GetDetailEventUseCase.Params(idEvent))
                .map {
                    EventUiState.Success(it.domainToUi())
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = EventUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )

        private suspend fun addLastVisitedEvent(idEvent: String) {
            addLastVisitedEventUseCase(
                AddLastVisitedEventUseCase.Params(
                    idEvent = idEvent,
                    lastVisited = Date().time,
                    countryCode = "MX",
                ),
            ).onFailure {
                Timber.e("addLastVisitedEvent -> $it")
            }
        }

        fun updateFavoriteEvent(event: EventUi) =
            viewModelScope.launch {
                val eventType =
                    if (event.eventType == EventType.Default) {
                        EventType.Favorite
                    } else {
                        EventType.Default
                    }

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
