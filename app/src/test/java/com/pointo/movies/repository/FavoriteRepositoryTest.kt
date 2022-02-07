@file:Suppress("SpellCheckingInspection")

package com.pointo.movies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pointo.movies.MainCoroutinesRule
import com.pointo.movies.MockUtil
import com.pointo.movies.db.MoviesDao
import com.pointo.movies.ui.favorite.FavoriteRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class FavoriteRepositoryTest {

    private lateinit var repository: FavoriteRepository
    private val dao: MoviesDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        repository = FavoriteRepository(dao)
    }

    @Test
    fun fetchFavoritesTest() = runBlocking {
        whenever(dao.getFavorites()).thenReturn(flowOf(listOf(MockUtil.mockSimpleMovie())))

        repository.getFavorites(
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test {
            val favoritedItem = awaitItem()[0]
            assertEquals(favoritedItem.id, MockUtil.mockSimpleMovie().id)
            assertEquals(favoritedItem.title, MockUtil.mockSimpleMovie().title)
            awaitComplete()
        }
    }
}
