package com.globant.ticketmaster.feature.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetEventsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetSuggestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EventsViewModel
    @Inject
    constructor(
        getEventsUseCase: GetEventsUseCase,
        private val getClassificationsUseCase: GetClassificationsUseCase,
        private val getSuggestionsUseCase: GetSuggestionsUseCase,
    ) : ViewModel() {
        init {
            viewModelScope.launch {
                launch {
                    getClassificationsUseCase(Unit).collect {
                        it.forEach {
                            Timber.i("getClassifications -> $it")
                        }
                    }
                }
                launch {
                    getSuggestionsUseCase(GetSuggestionsUseCase.Params("MX"))
                        .distinctUntilChanged()
                        .collect {
                            it.forEach {
                                Timber.i("getSuggestions -> $it")
                            }
                        }
                }
            }
        }

        val uiState: StateFlow<EventsUiState> =
            getEventsUseCase(GetEventsUseCase.Params("MX"))
                .map {
                    EventsUiState.Items(it, it)
                }.stateIn(
                    scope = viewModelScope,
                    initialValue = EventsUiState.Loading,
                    started = SharingStarted.WhileSubscribed(),
                )
    }
