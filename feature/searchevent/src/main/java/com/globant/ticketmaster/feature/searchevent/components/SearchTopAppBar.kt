package com.globant.ticketmaster.feature.searchevent.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchTopAppBar(
    search: TextFieldValue,
    onBackClick: () -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    TopAppBar(title = {
        TextField(
            modifier =
                Modifier
                    .fillMaxWidth()
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
                Box(Modifier.offset(x = 4.dp)) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                Box(Modifier.offset(x = (-4).dp)) {
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
                }
            },
        )
    }, navigationIcon = {
        IconButton(
            onClick = onBackClick,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
            )
        }
    })
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}
