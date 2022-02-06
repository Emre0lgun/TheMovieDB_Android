package com.pointo.movies.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.pointo.movies.data.models.LoginModel
import com.pointo.movies.util.Constants
import com.pointo.movies.util.combineWith
import com.skydoves.bindables.BindingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    datastore: DataStore<Preferences>
) :
    BindingViewModel() {

    private val timeoutMinutes = 2

    val usernameLiveData: LiveData<String?> = datastore.data.map { preferences ->
        preferences[Constants.datastoreKeyUsername]
    }.asLiveData()

    val timeLiveData: LiveData<Long?> = datastore.data.map { preferences ->
        preferences[Constants.datastoreKeyTime]
    }.asLiveData()

    val loginLiveData: LiveData<LoginModel?> =
        usernameLiveData.combineWith(timeLiveData) { username, time ->
            LoginModel(username, isLoginRequired(time))
        }

    private fun isLoginRequired(time: Long?): Boolean {
        time?.let {
            val diff: Long = Date().time - time
            val minutes = diff / 60000
            if (minutes < timeoutMinutes) {
                return false
            }
        }
        return true
    }
}

