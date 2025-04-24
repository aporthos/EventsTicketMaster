package com.globant.ticketmaster.core.data.datasources.events

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
class EventsRemoteDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var service: ApiServices

    private val eventsRemoteDataSource by lazy {
        EventsRemoteDataSourceImpl(service, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `validation get events`() =
        runTest {
            whenever(
                service.getEvents(
                    countryCode = "US",
                    keyword = "music",
                    page = 1,
                    idClassification = "KZFzniwnSyZfZ7v7nE",
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
                eventsRemoteDataSource.getEvents(
                    countryCode = "US",
                    keyword = "music",
                    page = 1,
                    idClassification = "KZFzniwnSyZfZ7v7nE",
                )

            assertEquals(result.isSuccess, true)
        }

    @Test
    fun `validation get events failure`() =
        runTest {
            whenever(
                service.getEvents(
                    countryCode = "US",
                    keyword = "music",
                    page = 1,
                    idClassification = "KZFzniwnSyZfZ7v7nE",
                ),
            ).thenThrow(RuntimeException())
            val result =
                eventsRemoteDataSource.getEvents(
                    countryCode = "US",
                    keyword = "music",
                    page = 1,
                    idClassification = "KZFzniwnSyZfZ7v7nE",
                )
            assertEquals(result.isFailure, true)
        }
}
