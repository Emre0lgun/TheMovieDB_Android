package com.pointo.movies.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pointo.movies.data.models.SimpleMovieModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies where isFavorite = 1")
    fun getFavorites(): Flow<List<SimpleMovieModel>>

    @Query("SELECT * FROM movies where isWatchlist = 1")
    fun getWatchlist(): Flow<List<SimpleMovieModel>>

    @Query("SELECT * FROM movies where id =:id")
    fun getDetail(id: Int): Flow<SimpleMovieModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSimpleMovie(movieModel: SimpleMovieModel)
}