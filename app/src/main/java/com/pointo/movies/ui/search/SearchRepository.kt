package com.pointo.movies.ui.search

import android.util.Log
import androidx.annotation.WorkerThread
import com.pointo.movies.data.responses.SearchResponse
import com.pointo.movies.network.MoviesService
import com.pointo.movies.network.NetworkRepository
import com.pointo.movies.network.NetworkResult
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SearchRepository @Inject constructor(private val moviesService: MoviesService) :
    NetworkRepository {

    @WorkerThread
    fun searchMovies(
        keyword: String,
        page: Int,
        onStart: () -> Unit,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<SearchResponse> {

        val result: NetworkResult<SearchResponse> = wrapNetworkResult(call = {
            moviesService.search(page = page, query = keyword)
        })

        when (result) {
            is NetworkResult.SuccessfulNetworkResult -> {
//                if (keyword.isNotEmpty()) {

//                    var searchResponse = result.body
//
//                    searchResponse.results  = searchResponse.results?.filter { it.title.lowercase().contains(keyword.lowercase()) }
//
////                    for (movie in searchResponse.results!!){
////                        if(movie.title.lowercase().contains(keyword.lowercase())){
////                            Log.e("Melo",movie.toString())
////                        }
//                    }
//
////                    Log.e("Melo",searchResponse.results?.filter { it.title.contains(keyword) }.toString())
//                    emit(searchResponse)
//                } else {
//                    emit(result.body)
//                }
                emit(result.body)
                onSuccess()
            }
            is NetworkResult.EmptyNetworkResult -> onError(result.customErrorMessage)
            is NetworkResult.ErrorNetworkResult -> {
                try {
                    val moshi = Moshi.Builder().build()
                    val jsonAdapter: JsonAdapter<SearchResponse> =
                        moshi.adapter(SearchResponse::class.java)
                    jsonAdapter.fromJson(result.errorMessage)?.let { emit(it) }

                } catch (e: Exception) {
                    onError(result.errorMessage)
                }
            }
            is NetworkResult.ExceptionResult -> onError(result.errorMessage)
        }

    }.onStart { onStart() }.flowOn(Dispatchers.IO)
}