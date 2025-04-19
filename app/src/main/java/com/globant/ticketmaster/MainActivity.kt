package com.globant.ticketmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.globant.ticketmaster.core.designsystem.theme.ChallengeTicketmasterTheme
import com.globant.ticketmaster.feature.events.EventsRoute
import com.globant.ticketmaster.feature.favorites.FavoritesRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberTicketMasterAppState()
            ChallengeTicketmasterTheme {
                TicketMasterAppNavHost(appState)
            }
        }
    }
}

@Composable
fun TicketMasterAppNavHost(appState: TicketMasterAppState) {
    NavHost(
        navController = appState.navController,
        startDestination = AppSections.Home,
    ) {
        composable<AppSections.Onboarding> {
        }
        composable<AppSections.Home> {
            HomeMainContainer(appState.bottomBarRoutes)
        }
    }
}

@Composable
fun HomeMainContainer(items: List<HomeSections>) {
    val appState = rememberTicketMasterAppState()
    val backStackEntry by appState.navController.currentBackStackEntryAsState()
    Scaffold(bottomBar = {
        MainBottomAppBar(
            navBackStackEntry = backStackEntry,
            items = items,
            navigateToRoute = appState::navigateToBottomBarRoute,
        )
    }, content = { paddingValues ->
        NavHost(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
            navController = appState.navController,
            startDestination = HomeSections.Events.route,
        ) {
            composable(HomeSections.Events.route) {
                EventsRoute()
            }
            composable(HomeSections.Favorites.route) {
                FavoritesRoute()
            }
        }
    })
}
