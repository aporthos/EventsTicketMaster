package com.globant.ticketmaster.feature.searchevent

data class SearchFilters(
    val keyword: String,
    val countryCode: String,
    val idClassification: String,
)
