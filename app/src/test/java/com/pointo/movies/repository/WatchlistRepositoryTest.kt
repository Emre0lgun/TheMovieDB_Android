@file:Suppress("SpellCheckingInspection")

package com.pointo.movies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pointo.movies.MainCoroutinesRule
import com.pointo.movies.MockUtil
import com.pointo.movies.db.MoviesDao
import com.pointo.movies.ui.watchlist.WatchlistRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class WatchlistRepositoryTest {

    private lateinit var repository: WatchlistRepository
    private val dao: MoviesDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        repository = WatchlistRepository(dao)
    }

    @Test
    fun fetchFavoritesTest() = runBlocking {
        whenever(dao.getWatchlist()).thenReturn(flowOf(listOf(MockUtil.mockSimpleMovie())))

        repository.getWatchlist(
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test {
            val watchlistItem = awaitItem()[0]
            assertEquals(watchlistItem.id, MockUtil.mockSimpleMovie().id)
            assertEquals(watchlistItem.title, MockUtil.mockSimpleMovie().title)
            awaitComplete()
        }
    }
}
