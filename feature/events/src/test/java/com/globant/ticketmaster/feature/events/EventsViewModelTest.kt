package com.globant.ticketmaster.feature.events

import app.cash.turbine.test
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetCountriesUseCase
import com.globant.ticketmaster.core.domain.usecases.GetSuggestionsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateCountryUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.RefreshSuggestionsUseCase
import com.globant.ticketmaster.core.testing.FakeClassificationsRepository
import com.globant.ticketmaster.core.testing.FakeDataStoreRepository
import com.globant.ticketmaster.core.testing.FakeEventsRepository
import com.globant.ticketmaster.core.testing.FakeSuggestionsRepository
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.classificationDomain
import com.globant.ticketmaster.core.testing.eventDomain
import com.globant.ticketmaster.core.testing.eventsUi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class EventsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val eventsResourcesManager = FakeEventsResourcesManager()

    private val classificationsRepository = FakeClassificationsRepository()

    private val eventsRepository = FakeEventsRepository()

    private val suggestionsRepository = FakeSuggestionsRepository()

    private val countriesRepository = FakeDataStoreRepository()
    private val viewModel =
        EventsViewModel(
            getSuggestionsEventsUseCase =
                GetSuggestionsUseCase(
                    suggestionsRepository = suggestionsRepository,
                    eventsRepository = eventsRepository,
                    mainDispatcherRule.testDispatcher,
                ),
            getClassificationsUseCase =
                GetClassificationsUseCase(
                    classificationsRepository,
                    mainDispatcherRule.testDispatcher,
                ),
            updateFavoriteEventUseCase =
                UpdateFavoriteEventUseCase(
                    eventsRepository,
                    mainDispatcherRule.testDispatcher,
                ),
            refreshSuggestionsUseCase =
                RefreshSuggestionsUseCase(
                    suggestionsRepository,
                    mainDispatcherRule.testDispatcher,
                ),
            eventsResourcesManager = eventsResourcesManager,
            getCountriesUseCase =
                GetCountriesUseCase(
                    countriesRepository,
                    mainDispatcherRule.testDispatcher,
                ),
            updateCountryUseCase =
                UpdateCountryUseCase(
                    countriesRepository,
                    mainDispatcherRule.testDispatcher,
                ),
        )

    @Test
    fun `validation flow classifications`() =
        runTest {
            viewModel.classificationsState.test {
                assertEquals(ClassificationsUiState.Loading, awaitItem())
                classificationsRepository.addClassification(
                    Result.success(
                        listOf(
                            classificationDomain,
                        ),
                    ),
                )
                assertEquals(
                    ClassificationsUiState.Items(listOf(classificationDomain)),
                    awaitItem(),
                )
            }
        }

    @Test
    fun `validation flow suggestions`() =
        runTest {
            viewModel.suggestionsEventsState.test {
                assertEquals(EventsUiState.Loading, awaitItem())
                suggestionsRepository.addEvent(Result.success(eventDomain))
                eventsRepository.addEvents(eventDomain)
                assertEquals(EventsUiState.Items(false, eventsUi, eventsUi), awaitItem())
            }
        }
}
