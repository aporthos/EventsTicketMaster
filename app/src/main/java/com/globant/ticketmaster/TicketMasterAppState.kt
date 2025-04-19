package com.globant.ticketmaster

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberTicketMasterAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        TicketMasterAppState(navController)
    }

@Stable
class TicketMasterAppState(
    val navController: NavHostController,
) {
    val bottomBarRoutes =
        listOf(
            HomeSections.Events,
            HomeSections.Favorites,
        )

    fun navigateToBottomBarRoute(route: String) {
        if (route != navController.currentDestination?.route) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = false
                    saveState = true
                }
            }
        }
    }

    fun upPress() {
        navController.navigateUp()
    }
}
