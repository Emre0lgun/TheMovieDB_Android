package com.pointo.movies.ui.login

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.pointo.movies.util.Constants
import com.skydoves.bindables.BindingViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val datastore: DataStore<Preferences>
) :
    BindingViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    var email: ObservableField<String> = ObservableField(Constants.STRING_EMPTY)
    var password: ObservableField<String> = ObservableField(Constants.STRING_EMPTY)

    fun onLoginClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            var isValid = true
            email.get()?.let {
                isValid = it.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(it).matches()
                if (!isValid)
                    eventChannel.send(Event.ShowNoEmailError)
            }

            password.get()?.let {
                if (it.length < 6) {
                    eventChannel.send(Event.ShowPasswordLengthError)
                    return@launch
                }

                val uppercaseChecker = Regex(".*[A-Z].*")

                if (!uppercaseChecker.matches(it)) {
                    eventChannel.send(Event.ShowPasswordNoUppercaseError)
                    return@launch
                }

                val digitChecker = Regex(".*[0-9].*")

                if (!digitChecker.matches(it)) {
                    eventChannel.send(Event.ShowPasswordNoDigitError)
                    return@launch
                }
            }

            if (isValid) {
                saveLoginInfoToDatabase()
            }
        }
    }

    fun saveLoginInfoToDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            email.get()?.let { text ->
                datastore.edit { preferences ->
                    preferences[Constants.datastoreKeyUsername] = text
                    preferences[Constants.datastoreKeyTime] = Date().time
                }
            }
            eventChannel.send(Event.NavigateToChat(email.get().toString()))
        }
    }

    sealed class Event {
        data class NavigateToChat(val username: String) : Event()
        object ShowNoEmailError : Event()
        object ShowPasswordLengthError : Event()
        object ShowPasswordNoUppercaseError : Event()
        object ShowPasswordNoDigitError : Event()
    }

}