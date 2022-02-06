package com.pointo.movies.util

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    const val STRING_EMPTY = ""

    const val datastoreName = "datastore"

    val datastoreKeyUsername = stringPreferencesKey("email")
    val datastoreKeyTime = longPreferencesKey("time")

}