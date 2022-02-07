package com.pointo.movies.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pointo.movies.data.models.SimpleMovieModel

@Database(
    version = 1,
    entities = [
        SimpleMovieModel::class,
    ]
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}