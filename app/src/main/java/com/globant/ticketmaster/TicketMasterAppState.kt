package com.globant.ticketmaster

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.rememberNavController
import com.globant.ticketmaster.feature.detailevent.DetailEvent
import com.globant.ticketmaster.feature.searchevent.SearchEvents

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

    fun navigateToDetailEvent(route: DetailEvent) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSearchEvents(route: SearchEvents) {
        navController.navigate(route) {
            navigate()
        }
    }

    private fun NavOptionsBuilder.navigate() {
        launchSingleTop = true
        restoreState = true
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
    }

    fun upPress() {
        navController.navigateUp()
    }
}
