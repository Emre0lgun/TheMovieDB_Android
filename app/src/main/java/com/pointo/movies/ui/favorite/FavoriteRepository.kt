package com.pointo.movies.ui.favorite

import androidx.annotation.WorkerThread
import com.pointo.movies.db.MoviesDao
import com.pointo.movies.network.NetworkRepository
import com.pointo.movies.util.toMovieModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val moviesDao: MoviesDao) :
    NetworkRepository {

    @WorkerThread
    fun getFavorites(
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        moviesDao.getFavorites().collect { list ->
            emit(list.map { it.toMovieModel() })
            onSuccess()
        }
    }.onStart { onStart() }.catch { onError(it.message) }.flowOn(Dispatchers.IO)
}


