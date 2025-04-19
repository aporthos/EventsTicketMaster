package com.globant.ticketmaster

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry

@Composable
fun MainBottomAppBar(
    navBackStackEntry: NavBackStackEntry?,
    items: List<HomeSections>,
    navigateToRoute: (String) -> Unit,
) {
    BottomAppBar {
        items.forEach { item ->
            val selected = item.route == navBackStackEntry?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { navigateToRoute(item.route) },
                icon = {
                    Icon(painter = painterResource(id = item.icon), contentDescription = null)
                },
                label = {
                    Text(
                        text = stringResource(id = item.title),
                    )
                },
            )
        }
    }
}