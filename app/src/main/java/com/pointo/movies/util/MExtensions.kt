package com.pointo.movies.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.pointo.movies.data.models.Movie
import com.pointo.movies.data.models.SimpleMovieModel
import com.pointo.movies.data.responses.DetailResponse
import com.pointo.movies.util.Constants.datastoreName
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

operator fun <T> MutableLiveData<MutableList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}

class FlowObserver<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>,
    private val collector: suspend (T) -> Unit
) {

    private var job: Job? = null

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect { collector(it) }
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }
                else -> {
                }
            }
        })
    }
}

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this, {})

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    var counter = 0
    result.addSource(this) {
        counter++
        if (counter == 2)
            result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        counter++
        if (counter == 2)
            result.value = block(this.value, liveData.value)
    }

    return result
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = datastoreName)

fun View.hide() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.show() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun Fragment.shortToast(text: String) {
    if (text.isNotBlank())
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

@ExperimentalCoroutinesApi
@CheckResult
fun EditText.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                trySend(s)
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}


fun Movie.toMovieDetailModel(): DetailResponse {

    return DetailResponse(
        adult = this.adult,
        backdrop_path = this.backdrop_path,
        belongs_to_collection = null,
        budget = null,
        genres = null,
        homepage = null,
        id = this.id,
        imdb_id = null,
        original_language = this.original_language,
        original_title = this.original_title,
        overview = this.overview,
        popularity = this.popularity,
        poster_path = this.poster_path,
        production_companies = null,
        production_countries = null,
        release_date = this.release_date,
        revenue = null,
        runtime = null,
        spoken_languages = null,
        status = null,
        tagline = null,
        title, video, vote_average, vote_count
    )
}

fun SimpleMovieModel.toMovieModel(): Movie {
    return Movie(
        adult = false,
        backdrop_path = null,
        genre_ids = listOf(),
        id = this.id,
        original_language = "",
        original_title = "",
        overview = "",
        popularity = 0.0,
        poster_path = poster_path,
        release_date = null,
        title = this.title,
        video = false,
        vote_average = 0.0,
        vote_count = 0
    )
}

fun Movie.toSimpleMovieModel(): SimpleMovieModel {
    return SimpleMovieModel(
        id = id,
        title = title,
        poster_path = poster_path,
        isFavorite = false,
        isWatchlist = false
    )
}


fun String.toDate(pattern: String, locale: Locale = Locale.getDefault()): Date? =
    SimpleDateFormat(pattern, locale).parse(this)

fun Date.format(pattern: String, locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat(pattern, locale).format(this)


fun Context.isConnected(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
        else -> {
            // Use depreciated methods only on older devices
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            nwInfo.isConnected
        }
    }
}

