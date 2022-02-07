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
import com.pointo.movies.data.models.Movie
import com.pointo.movies.ui.favorite.FavoriteRepository
import com.pointo.movies.ui.favorite.FavoriteViewModel
import com.pointo.movies.util.toMovieModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteRepository: FavoriteRepository
    private val dao: MoviesDao = mock()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        favoriteRepository = FavoriteRepository(dao)
        val savedStateHandle = SavedStateHandle()
        savedStateHandle.set("movie", MockUtil.mockMovie())
        viewModel = FavoriteViewModel(favoriteRepository)
    }


    @Test
    fun fetchFavoriteDetailTest() = runBlocking {
        val mockData = listOf(MockUtil.mockSimpleMovie().toMovieModel())
        whenever(dao.getFavorites()).thenReturn(flowOf(listOf(MockUtil.mockSimpleMovie())))

        val observer: Observer<List<Movie>> = mock()
        val fetchedData: LiveData<List<Movie>> =
            favoriteRepository.getFavorites(
                onStart = {},
                onSuccess = {},
                onError = {}).asLiveData()
        fetchedData.observeForever(observer)

        delay(500L)

        verify(observer).onChanged(mockData)
        fetchedData.removeObserver(observer)
    }


}