package com.globant.ticketmaster.core.data.datasources.events

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.database.daos.EventsDao
import com.globant.ticketmaster.core.database.daos.FavoritesEventsDao
import com.globant.ticketmaster.core.database.daos.LastVisitedEventsDao
import com.globant.ticketmaster.core.database.daos.VenuesDao
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.eventsDomain
import com.globant.ticketmaster.core.testing.lastVisitedEntity
import com.globant.ticketmaster.core.testing.suggestedEntity
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
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class EventsLocalDataSourceImplTest {
    @Mock
    private lateinit var eventsDao: EventsDao

    @Mock
    private lateinit var venuesDao: VenuesDao

    @Mock
    private lateinit var lastVisitedEventsDao: LastVisitedEventsDao

    @Mock
    private lateinit var favoritesEventsDao: FavoritesEventsDao

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val localDataSource by lazy {
        EventsLocalDataSourceImpl(
            eventsDao,
            venuesDao,
            lastVisitedEventsDao,
            favoritesEventsDao,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `validation get last visited events`() =
        runTest {
            whenever(lastVisitedEventsDao.getLastVisitedEvents("US")).thenReturn(
                flowOf(
                    lastVisitedEntity,
                ),
            )
            val result = localDataSource.getLastVisitedEvents("US")
            assertEquals(result.first().size, eventsDomain.size)
        }

    @Test
    fun `validation get suggested events`() =
        runTest {
            whenever(eventsDao.getSuggestedEvents(listOf("123", "456"))).thenReturn(
                flowOf(
                    suggestedEntity,
                ),
            )
            val result = localDataSource.getSuggestedEvents(listOf("123", "456"))
            assertEquals(result.first().size, eventsDomain.size)
        }

    @Test
    fun `validation set favorite event`() =
        runTest {
            whenever(
                favoritesEventsDao.insertOrIgnore(any()),
            ).thenReturn(1)
            val result = localDataSource.setFavoriteEvent("123", EventType.Default)
            assertEquals(result, true)
        }

    @Test
    fun `validation set last visited event`() =
        runTest {
            whenever(
                lastVisitedEventsDao.insertOrIgnore(any()),
            ).thenReturn(1)
            val result = localDataSource.setLastVisitedEvent("123", 123, "US")
            assertEquals(result, true)
        }
}
