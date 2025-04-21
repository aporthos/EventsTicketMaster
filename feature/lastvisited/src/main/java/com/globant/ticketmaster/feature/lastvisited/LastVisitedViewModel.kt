package com.globant.ticketmaster.feature.lastvisited

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.GetLastVisitedEventsPagingUseCase
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LastVisitedViewModel
    @Inject
    constructor(
        private val updateFavoriteEventUseCase: UpdateFavoriteEventUseCase,
        getLastVisitedEventsPagingUseCase: GetLastVisitedEventsPagingUseCase,
    ) : ViewModel() {
        private val search = MutableStateFlow("")

        val eventsPagingState =
            search
                .debounce(500)
                .flatMapLatest { filters ->
                    getLastVisitedEventsPagingUseCase(
                        GetLastVisitedEventsPagingUseCase.Params(
                            filters,
                            "MX",
                        ),
                    )
                }.map { paging ->
                    paging.map(Event::domainToUi)
                }.cachedIn(viewModelScope)

        fun onSearch(search: String) {
            this.search.value = search
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
