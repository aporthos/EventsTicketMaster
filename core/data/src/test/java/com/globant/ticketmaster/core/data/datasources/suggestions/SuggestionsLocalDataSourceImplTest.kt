package com.globant.ticketmaster.core.data.datasources.suggestions

import com.globant.ticketmaster.core.database.daos.SuggestionsEventsDao
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.eventsDomain
import com.globant.ticketmaster.core.testing.suggestionsWithEventsEntity
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
class SuggestionsLocalDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var suggestionsDao: SuggestionsEventsDao

    private val localDataSource by lazy {
        SuggestionsLocalDataSourceImpl(
            suggestionsDao,
            mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun `validation get suggestions events`() =
        runTest {
            whenever(suggestionsDao.getSuggestionsEvents("US")).thenReturn(
                flowOf(
                    listOf(
                        suggestionsWithEventsEntity,
                        suggestionsWithEventsEntity,
                        suggestionsWithEventsEntity,
                    ),
                ),
            )
            val result = localDataSource.getSuggestionsEvents("US")
            assertEquals(result.first().size, 3)
        }

    @Test
    fun `validation add suggestions events`() =
        runTest {
            whenever(suggestionsDao.insertOrIgnore(any())).thenReturn(
                listOf(1L),
            )
            val result = localDataSource.addSuggestionsEvents(eventsDomain)
            assertEquals(result, true)
        }
}
