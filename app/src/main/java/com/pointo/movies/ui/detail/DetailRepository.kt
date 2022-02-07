package com.pointo.movies.ui.detail

import androidx.annotation.WorkerThread
import com.pointo.movies.db.MoviesDao
import com.pointo.movies.data.models.SimpleMovieModel
import com.pointo.movies.data.responses.DetailResponse
import com.pointo.movies.network.MoviesService
import com.pointo.movies.network.NetworkRepository
import com.pointo.movies.network.NetworkResult
import com.pointo.movies.util.Constants.emptyErrorText
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class DetailRepository @Inject constructor(
    private val moviesService: MoviesService,
    private val moviesDao: MoviesDao
) :
    NetworkRepository {

    @WorkerThread
    fun detail(
        movieId: Int,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<DetailResponse> {

        val result: NetworkResult<DetailResponse> = wrapNetworkResult(call = {
            moviesService.detail(movieId = movieId)
        }, emptyErrorText)

        when (result) {
            is NetworkResult.SuccessfulNetworkResult -> {
                emit(result.body)
                onSuccess()
            }
            is NetworkResult.EmptyNetworkResult -> onError(result.customErrorMessage)
            is NetworkResult.ErrorNetworkResult -> {
                try {
                    val moshi = Moshi.Builder().build()
                    val jsonAdapter: JsonAdapter<DetailResponse> =
                        moshi.adapter(DetailResponse::class.java)
                    jsonAdapter.fromJson(result.errorMessage)?.let { emit(it) }

                } catch (e: Exception) {
                    onError(result.errorMessage)
                }
            }
            is NetworkResult.ExceptionResult -> onError(result.errorMessage)
        }

    }.onStart { onStart() }.flowOn(Dispatchers.IO)

    @WorkerThread
    fun getFavoriteWatchListInfo(
        id: Int,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<SimpleMovieModel> {
        moviesDao.getDetail(id).collect { list ->
            emit(list)
            onSuccess()
        }
    }.onStart { onStart() }.catch { onError(it.message) }.flowOn(Dispatchers.IO)

    @WorkerThread
    suspend fun addSimpleMovie(model: SimpleMovieModel) {
        moviesDao.addSimpleMovie(model)
    }
}