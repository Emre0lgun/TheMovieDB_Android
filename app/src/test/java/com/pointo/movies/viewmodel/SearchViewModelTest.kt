package com.pointo.movies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.pointo.movies.MainCoroutinesRule
import com.pointo.movies.MockUtil
import com.pointo.movies.data.responses.SearchResponse
import com.pointo.movies.network.MoviesService
import com.pointo.movies.ui.search.SearchRepository
import com.pointo.movies.ui.search.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify
import retrofit2.Response

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var searchRepository: SearchRepository
    private val moviesService: MoviesService = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        searchRepository = SearchRepository(moviesService)
        viewModel = SearchViewModel(searchRepository)
    }

    @Test
    fun fetchMovieSearchTest() = runBlocking {
        val mockData = MockUtil.mockSearchResponse()
        whenever(
            moviesService.search(
                "Marvel",
                1
            )
        ).thenReturn(Response.success(mockData))

        val observer: Observer<SearchResponse> = mock()
        val fetchedData: LiveData<SearchResponse> =
            searchRepository.searchMovies(
                keyword = "Marvel",
                page = 1,
                onStart = {},
                onSuccess = {},
                onError = {}).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchSearchedMovies()
        delay(500L)

        verify(observer).onChanged(mockData)
        fetchedData.removeObserver(observer)
    }


}