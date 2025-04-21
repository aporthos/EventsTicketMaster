package com.globant.ticketmaster.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.domain.usecases.GetFavoritesEventsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.models.ui.domainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
        private val search = MutableStateFlow("")

        val uiState: Flow<PagingData<EventUi>> =
            search
                .debounce(500)
                .flatMapLatest { search ->
                    getFavoritesEventsUseCase(GetFavoritesEventsUseCase.Params(search, "MX"))
                }.map { paging ->
                    paging.map(Event::domainToUi)
                }.cachedIn(viewModelScope)

        fun onSearch(search: String) {
            this.search.value = search
        }

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
