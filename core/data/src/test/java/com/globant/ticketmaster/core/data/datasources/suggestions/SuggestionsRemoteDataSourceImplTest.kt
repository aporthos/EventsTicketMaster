package com.globant.ticketmaster.core.data.datasources.suggestions

import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.models.network.BaseResponseNetwork
import com.globant.ticketmaster.core.models.network.EmbeddedNetwork
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.eventNetwork
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class SuggestionsRemoteDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var service: ApiServices

    private val suggestionsRemoteDataSource by lazy {
        SuggestionsRemoteDataSourceImpl(service, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `validation get suggestions`() =
        runTest {
            whenever(
                service.getSuggestions(
                    countryCode = "US",
                    segmentId = "KZFzniwnSyZfZ7v7nE",
                ),
            ).thenReturn(
                BaseResponseNetwork(
                    embedded =
                        EmbeddedNetwork(
                            events = listOf(eventNetwork),
                        ),
                    links = null,
                    page = null,
                ),
            )

            val result =
                suggestionsRemoteDataSource.getSuggestionsEvents(
                    countryCode = "US",
                    idSegments = "KZFzniwnSyZfZ7v7nE",
                )
            assertEquals(result.isSuccess, true)
        }

    @Test
    fun `validation get suggestions failure`() =
        runTest {
            whenever(
                service.getSuggestions(
                    countryCode = "US",
                    segmentId = "KZFzniwnSyZfZ7v7nE",
                ),
            ).thenThrow(RuntimeException())

            val result =
                suggestionsRemoteDataSource.getSuggestionsEvents(
                    countryCode = "US",
                    idSegments = "KZFzniwnSyZfZ7v7nE",
                )
            assertEquals(result.isFailure, true)
        }
}
