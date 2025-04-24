package com.globant.ticketmaster.feature.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.globant.ticketmaster.feature.countries.CountriesUiState
import com.globant.ticketmaster.feature.countries.MenuCountries

@Composable
fun SearchTopAppBar(
    countriesState: CountriesUiState,
    search: TextFieldValue,
    onEvents: (FavoritesEvents) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            TextField(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .focusRequester(focusRequester),
                singleLine = true,
                colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                value = search,
                shape = SearchBarDefaults.inputFieldShape,
                onValueChange = {
                    if (it.text.length <= 30) {
                        onSearch(it)
                    }
                },
                placeholder = {
                    Text(text = "Buscar eventos")
                },
                leadingIcon = {
                    Box {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                        )
                    }
                },
                trailingIcon = {
                    if (search.text.isNotEmpty()) {
                        IconButton(onClick = {
                            onSearch(TextFieldValue(""))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        },
        actions = {
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
                            onEvents(FavoritesEvents.OnSelectCountry(country))
                        },
                        onDismiss = { expanded = false },
                    )
                }
            }
        },
    )

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}
