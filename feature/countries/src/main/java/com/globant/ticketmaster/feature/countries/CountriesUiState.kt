package com.globant.ticketmaster.feature.countries

import com.globant.ticketmaster.core.models.ui.CountryEventUi

sealed interface CountriesUiState {
    data class Success(
        val current: CountryEventUi,
        val countries: List<CountryEventUi>,
    ) : CountriesUiState

    data object Loading : CountriesUiState
}