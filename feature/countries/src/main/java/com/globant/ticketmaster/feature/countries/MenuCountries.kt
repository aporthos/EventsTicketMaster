package com.globant.ticketmaster.feature.countries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.globant.ticketmaster.core.models.ui.CountryEventUi

@Composable
fun MenuCountries(
    expanded: Boolean,
    countriesState: List<CountryEventUi>,
    onClick: (CountryEventUi) -> Unit,
    onDismiss: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDismiss() },
        ) {
            countriesState.forEach { country ->
                DropdownMenuItem(
                    text = { Text(text = country.name) },
                    leadingIcon = {
                        if (country.isSelected) {
                            Icon(
                                Icons.Outlined.Check,
                                null,
                            )
                        }
                    },
                    trailingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = country.icon),
                            tint = Color.Unspecified,
                            contentDescription = null,
                        )
                    },
                    onClick = { onClick(country) },
                )
            }
        }
    }
}
