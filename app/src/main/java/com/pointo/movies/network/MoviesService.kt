package com.pointo.movies.network

import com.pointo.movies.data.responses.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("search/movie/")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<SearchResponse>
}
