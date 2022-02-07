@file:Suppress("SpellCheckingInspection")

package com.pointo.movies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pointo.movies.MainCoroutinesRule
import com.pointo.movies.MockUtil.mockDetailResponse
import com.pointo.movies.db.MoviesDao
import com.pointo.movies.network.MoviesService
import com.pointo.movies.ui.detail.DetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class DetailRepositoryTest {

    private lateinit var repository: DetailRepository
    private val service: MoviesService = mock()
    private val dao: MoviesDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        repository = DetailRepository(service, dao)
    }


    @Test
    fun fetchMovieDetailTest() = runBlocking {
        val mockData = mockDetailResponse()
        whenever(service.detail(122)).thenReturn(Response.success(mockData))

        repository.detail(
            movieId = 122,
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test {
            assertEquals(awaitItem().runtime, 201)
            awaitComplete()
        }
    }

}
