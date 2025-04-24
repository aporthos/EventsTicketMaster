package com.globant.ticketmaster.feature.events

import app.cash.turbine.test
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.domain.usecases.GetSuggestionsUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.RefreshSuggestionsUseCase
import com.globant.ticketmaster.core.testing.FakeClassificationsRepository
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

    private val getSuggestionsUseCase =
        GetSuggestionsUseCase(
            suggestionsRepository = suggestionsRepository,
            eventsRepository = eventsRepository,
            mainDispatcherRule.testDispatcher,
        )

    private val getClassificationsUseCase =
        GetClassificationsUseCase(classificationsRepository, mainDispatcherRule.testDispatcher)

    private val updateFavoriteEventUseCase =
        UpdateFavoriteEventUseCase(eventsRepository, mainDispatcherRule.testDispatcher)

    private val refreshSuggestionsUseCase =
        RefreshSuggestionsUseCase(suggestionsRepository, mainDispatcherRule.testDispatcher)

    private val viewModel =
        EventsViewModel(
            getSuggestionsEventsUseCase = getSuggestionsUseCase,
            getClassificationsUseCase = getClassificationsUseCase,
            updateFavoriteEventUseCase = updateFavoriteEventUseCase,
            refreshSuggestionsUseCase = refreshSuggestionsUseCase,
            eventsResourcesManager = eventsResourcesManager,
        )

    @Test
    fun `validation flow classifications`() =
        runTest {
            viewModel.classificationsState.test {
                assertEquals(ClassificationsUiState.Loading, awaitItem())
                classificationsRepository.addClassification(listOf(classificationDomain))
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
