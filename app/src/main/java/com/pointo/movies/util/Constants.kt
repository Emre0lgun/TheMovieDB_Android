package com.pointo.movies.util

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val baseUrl = "https://api.themoviedb.org/3/"
    const val baseImageUrl = "https://image.tmdb.org/t/p/w500"

    const val argKeyMovie = "movie"
    const val argKeyEmail = "email"

    const val emptyErrorText = "Empty Error"
    const val noConnectionErrorText = "There is no internet connection"

    const val STRING_EMPTY = ""

    const val datastoreName = "datastore"
    const val keyCache = "movies_cache"

    val datastoreKeyUsername = stringPreferencesKey("email")
    val datastoreKeyTime = longPreferencesKey("time")

}