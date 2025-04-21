package com.globant.ticketmaster.feature.searchevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetEventsPagingUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchEventsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getEventsPagingUseCase: GetEventsPagingUseCase,
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
    ) : ViewModel() {
        private val idClassification = savedStateHandle.toRoute<SearchEvents>().idClassification
        private val searchFilters =
            MutableStateFlow(
                SearchFilters(
                    keyword = "",
                    countryCode = "MX",
                    idClassification = idClassification,
                ),
            )

        val eventsPagingState =
            searchFilters
                .debounce(500)
                .flatMapLatest { filters ->
                    val params =
                        GetEventsPagingUseCase.Params(
                            countryCode = filters.countryCode,
                            keyword = filters.keyword,
                            idClassification = filters.idClassification,
                        )
                    getEventsPagingUseCase(params)
                }.map { paging ->
                    paging.map(Event::domainToUi)
                }.cachedIn(viewModelScope)

        fun onSearch(search: String) {
            this.searchFilters.update {
                it.copy(keyword = search)
            }
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
