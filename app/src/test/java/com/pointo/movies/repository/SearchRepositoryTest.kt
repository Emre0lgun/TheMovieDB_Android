@file:Suppress("SpellCheckingInspection")

package com.pointo.movies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pointo.movies.MainCoroutinesRule
import com.pointo.movies.MockUtil.mockSearchResponse
import com.pointo.movies.network.MoviesService
import com.pointo.movies.ui.search.SearchRepository
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SearchRepositoryTest {

    private lateinit var repository: SearchRepository
    private val service: MoviesService = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        repository = SearchRepository(service)
    }

    @Test
    fun searchMoviesTest() = runBlocking {
        val mockResponse = mockSearchResponse()
        whenever(service.search("Lord of the Rings", 1)).thenReturn(Response.success(mockResponse))
        val mockModel = mockResponse.results!![0]

        repository.searchMovies(
            keyword = "Lord of the Rings",
            page = 1,
            onStart = {},
            onSuccess = {},
            onError = {}
        ).test {
            assertEquals(awaitItem().results?.get(0)?.id, mockModel.id)
            awaitComplete()
        }
    }
}