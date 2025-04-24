package com.globant.ticketmaster.feature.detailevent

import app.cash.turbine.test
import com.globant.ticketmaster.core.domain.usecases.GetCountrySelectedUseCase
import com.globant.ticketmaster.core.domain.usecases.GetDetailEventUseCase
import com.globant.ticketmaster.core.domain.usecases.UpdateFavoriteEventUseCase
import com.globant.ticketmaster.core.domain.usecases.lastvisited.AddLastVisitedEventUseCase
import com.globant.ticketmaster.core.testing.FakeDataStoreRepository
import com.globant.ticketmaster.core.testing.FakeEventsRepository
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.SavedStateHandleRule
import com.globant.ticketmaster.core.testing.eventDomain
import com.globant.ticketmaster.core.testing.eventUi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class DetailEventViewModelTest {
    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(DetailEvent("1AfZkA8GkdnbxmD"))

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var addLastVisitedEventUseCase: AddLastVisitedEventUseCase

    @MockK
    private lateinit var updateFavoriteEventUseCase: UpdateFavoriteEventUseCase

    private val eventsRepository = FakeEventsRepository()

    private val getDetailEventUseCase =
        GetDetailEventUseCase(eventsRepository, mainDispatcherRule.testDispatcher)

    private lateinit var viewModel: DetailEventViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        val mocked = mockk<Result<Boolean>>()
        coEvery {
            addLastVisitedEventUseCase(
                any(),
            )
        } returns mocked

        viewModel =
            DetailEventViewModel(
                savedStateHandle = savedStateHandleRule.savedStateHandleMock,
                getDetailEventUseCase = getDetailEventUseCase,
                addLastVisitedEventUseCase = addLastVisitedEventUseCase,
                updateFavoriteEventUseCase = updateFavoriteEventUseCase,
                getCountrySelectedUseCase = GetCountrySelectedUseCase(FakeDataStoreRepository()),
            )
    }

    @Test
    fun `validation parameters detail event`() =
        runTest {
            assertEquals("1AfZkA8GkdnbxmD", viewModel.idEvent)
        }

    @Test
    fun `validation loading event`() =
        runTest {
            viewModel.eventState.test {
                assertEquals(EventUiState.Loading, awaitItem())
                eventsRepository.addEvents(eventDomain)
                assertEquals(EventUiState.Success(event = eventUi), awaitItem())
            }
        }
}
