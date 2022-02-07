package com.pointo.movies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.pointo.movies.MainCoroutinesRule
import com.pointo.movies.MockUtil
import com.pointo.movies.db.MoviesDao
import com.pointo.movies.data.responses.DetailResponse
import com.pointo.movies.network.MoviesService
import com.pointo.movies.ui.detail.DetailRepository
import com.pointo.movies.ui.detail.DetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private val moviesService: MoviesService = mock()
    private val dao: MoviesDao = mock()
    private var handle: SavedStateHandle = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        detailRepository = DetailRepository(moviesService, dao)
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.set("movie", MockUtil.mockMovie())
        viewModel = DetailViewModel(detailRepository, savedStateHandle)
    }

    @Test
    fun fetchMovieDetailTest() = runBlocking {
        val mockData = MockUtil.mockDetailResponse()
        whenever(moviesService.detail(122)).thenReturn(Response.success(mockData))

        val observer: Observer<DetailResponse> = mock()
        val fetchedData: LiveData<DetailResponse> =
            detailRepository.detail(
                movieId = 122,
                onStart = {},
                onSuccess = {},
                onError = {}).asLiveData()
        fetchedData.observeForever(observer)

        viewModel.fetchMovieDetail()
        delay(500L)

        verify(observer).onChanged(mockData)
        fetchedData.removeObserver(observer)
    }


}