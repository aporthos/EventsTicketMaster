package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.events.EventsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.suggestions.SuggestionsRemoteDataSource
import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.eventDomain
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class SuggestionsRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var remote: SuggestionsRemoteDataSource

    @Mock
    private lateinit var localEvents: EventsLocalDataSource

    @Mock
    private lateinit var localSuggestions: SuggestionsLocalDataSource

    @Mock
    private lateinit var classifications: ClassificationsLocalDataSource

    private val repository by lazy {
        SuggestionsRepositoryImpl(
            remote,
            localEvents,
            localSuggestions,
            classifications,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `validation get suggestions`() =
        runTest {
            whenever(localSuggestions.getSuggestionsEvents("US")).thenReturn(
                flowOf(
                    eventDomain,
                ),
            )

            val result = repository.getSuggestionsStream("US")

            assertEquals(
                result.first().isSuccess,
                true,
            )
        }

    @Test
    fun `validation refresh suggestions`() =
        runTest {
            whenever(classifications.getClassificationsStream()).thenReturn(
                flowOf(
                    listOf(
                        Classification(
                            idClassification = "KZFzniwnSyZfZ7v7nE",
                            name = "Music",
                        ),
                    ),
                ),
            )

            whenever(remote.getSuggestionsEvents("US", "KZFzniwnSyZfZ7v7nE")).thenReturn(
                Result.success(
                    eventDomain,
                ),
            )

            val result = repository.refreshSuggestions("US")

            assertEquals(
                result.isSuccess,
                true,
            )
            verify(localEvents).addEvents(any())
            verify(localSuggestions).addSuggestionsEvents(any())
        }

    @Test
    fun `validation refresh suggestions error`() =
        runTest {
            whenever(classifications.getClassificationsStream()).thenReturn(
                flowOf(
                    listOf(
                        Classification(
                            idClassification = "KZFzniwnSyZfZ7v7nE",
                            name = "Music",
                        ),
                    ),
                ),
            )
            whenever(remote.getSuggestionsEvents("US", "KZFzniwnSyZfZ7v7nE")).thenReturn(
                Result.failure(RuntimeException()),
            )

            val result = repository.refreshSuggestions("US")

            assertEquals(
                result.isFailure,
                true,
            )
            verify(localEvents, never()).addEvents(any())
            verify(localSuggestions, never()).addSuggestionsEvents(any())
        }
}
