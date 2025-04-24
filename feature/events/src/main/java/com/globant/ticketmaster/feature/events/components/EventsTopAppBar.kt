package com.globant.ticketmaster.feature.events.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.globant.ticketmaster.feature.countries.CountriesUiState
import com.globant.ticketmaster.feature.countries.MenuCountries
import com.globant.ticketmaster.feature.events.EventsUiEvents
import com.globant.ticketmaster.feature.events.R

@Composable
fun EventsTopAppBar(
    countriesState: CountriesUiState,
    onEvents: (EventsUiEvents) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text(text = stringResource(R.string.title_events)) },
        actions = {
            IconButton(onClick = {
                onEvents(EventsUiEvents.NavigateToSearch)
            }) {
                Icon(
                    painter = painterResource(id = com.globant.ticketmaster.core.designsystem.R.drawable.search),
                    contentDescription = null,
                )
            }

            IconButton(onClick = {
                expanded = true
            }) {
                if (countriesState is CountriesUiState.Success) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = countriesState.current.icon),
                        tint = Color.Unspecified,
                        contentDescription = null,
                    )
                    MenuCountries(
                        expanded = expanded,
                        countriesState = countriesState.countries,
                        onClick = { country ->
                            expanded = false
                            onEvents(EventsUiEvents.OnSelectCountry(country))
                        },
                        onDismiss = { expanded = false },
                    )
                }
            }
        },
    )
}
