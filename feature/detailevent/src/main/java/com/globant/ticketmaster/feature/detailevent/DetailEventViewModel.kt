package com.globant.ticketmaster.feature.detailevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetDetailEventUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        getDetailEventUseCase: GetDetailEventUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
    ) : ViewModel() {
        private val idEvent = savedStateHandle.toRoute<DetailEvent>().idEvent

        val eventState =
            getDetailEventUseCase(GetDetailEventUseCase.Params(idEvent))
                .map {
                    EventUiState.Success(it.domainToUi())
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = EventUiState.Loading,
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
