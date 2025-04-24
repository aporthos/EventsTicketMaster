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
import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.feature.detailevent.DetailEvent
import com.globant.ticketmaster.feature.detailevent.DetailEventRoute
import com.globant.ticketmaster.feature.events.EventsRoute
import com.globant.ticketmaster.feature.favorites.FavoritesRoute
import com.globant.ticketmaster.feature.lastvisited.LastVisited
import com.globant.ticketmaster.feature.lastvisited.LastVisitedRoute
import com.globant.ticketmaster.feature.searchevent.SearchEvents
import com.globant.ticketmaster.feature.searchevent.SearchEventsRoute
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
            HomeMainContainer(
                items = appState.bottomBarRoutes,
                navigateToDetailEvent = {
                    appState.navigateToDetailEvent(DetailEvent(it.idEvent))
                },
                navigateToClassification = {
                    appState.navigateToSearchEvents(SearchEvents(it.idClassification))
                },
                navigateToSearch = {
                    appState.navigateToSearchEvents(SearchEvents(""))
                },
                navigateToLastVisited = {
                    appState.navigateToLastVisitedEvent(LastVisited)
                },
            )
        }

        composable<DetailEvent> {
            DetailEventRoute(onBackClick = appState::upPress)
        }

        composable<SearchEvents> {
            SearchEventsRoute(
                onEventClick = {
                    appState.navigateToDetailEvent(DetailEvent(it.idEvent))
                },
                onBackClick = appState::upPress,
            )
        }

        composable<LastVisited> {
            LastVisitedRoute(
                onBackClick = appState::upPress,
                onEventClick = {
                    appState.navigateToDetailEvent(DetailEvent(it.idEvent))
                },
            )
        }
    }
}

@Composable
fun HomeMainContainer(
    items: List<HomeSections>,
    navigateToDetailEvent: (EventUi) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToClassification: (Classification) -> Unit,
    navigateToLastVisited: () -> Unit,
) {
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
                EventsRoute(
                    navigateToDetailEvent = navigateToDetailEvent,
                    navigateToSearch = navigateToSearch,
                    navigateToClassification = navigateToClassification,
                    navigateToLastVisited = navigateToLastVisited,
                )
            }
            composable(HomeSections.Favorites.route) {
                FavoritesRoute(navigateToDetailEvent)
            }
        }
    })
}
