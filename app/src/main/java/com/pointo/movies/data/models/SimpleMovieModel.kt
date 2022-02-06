package com.pointo.movies.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Movies")

data class SimpleMovieModel(
    @PrimaryKey val id: Int,
    val title: String,
    val poster_path: String?,
    var isFavorite: Boolean,
    var isWatchlist: Boolean
)