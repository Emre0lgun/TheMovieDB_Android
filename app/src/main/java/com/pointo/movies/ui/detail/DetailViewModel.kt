package com.pointo.movies.ui.detail

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.pointo.movies.data.models.Movie
import com.pointo.movies.data.models.SimpleMovieModel
import com.pointo.movies.data.responses.DetailResponse
import com.pointo.movies.util.Constants
import com.pointo.movies.util.Constants.argKeyMovie
import com.pointo.movies.util.toMovieDetailModel
import com.pointo.movies.util.toSimpleMovieModel
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val detailRepository: DetailRepository,
    state: SavedStateHandle
) : BindingViewModel() {

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    var detailResponse: LiveData<DetailResponse> = MutableLiveData()
    var movieModel: MutableLiveData<DetailResponse> = MutableLiveData()

    var simpleMovieModel: MutableLiveData<SimpleMovieModel> = MutableLiveData<SimpleMovieModel>()

    var errorMessage: MutableLiveData<String?> = MutableLiveData(Constants.STRING_EMPTY)

    val movie = state.get<Movie>(argKeyMovie)

    @get:Bindable
    var isLoading: Boolean by bindingProperty(true)
        private set

    init {
        movieModel.value = movie?.toMovieDetailModel()
        fetchMovieDetail()
    }

    fun fetchMovieDetail() {
        movie?.let {
            detailResponse = detailRepository.detail(movieId = movie.id,
                onStart = { isLoading = true },
                onSuccess = { isLoading = false },
                onError = { errorMessage.postValue(it) })
                .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

            viewModelScope.launch(Dispatchers.IO) {
                detailRepository.getFavoriteWatchListInfo(id = movie.id,
                    onStart = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { errorMessage.postValue(it) })
                    .collect {
                        simpleMovieModel.postValue(it)
                    }
            }
        }


    }

    fun favoriteClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            if (simpleMovieModel.value == null) {
                movie?.let {
                    val model = it.toSimpleMovieModel().apply { isFavorite = true }
                    detailRepository.addSimpleMovie(model)
                    simpleMovieModel.postValue(model)
                    eventChannel.send(Event.AddedToTheFavorites)
                }
            } else {
                simpleMovieModel.value?.apply {
                    isFavorite = !isFavorite
                    detailRepository.addSimpleMovie(this)
                    if (isFavorite)
                        eventChannel.send(Event.AddedToTheFavorites)
                    else
                        eventChannel.send(Event.RemovedFromTheFavorites)
                }
            }
        }
    }

    fun watchlistClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            if (simpleMovieModel.value == null) {
                movie?.let {
                    val model = it.toSimpleMovieModel().apply { isWatchlist = true }
                    detailRepository.addSimpleMovie(model)
                    simpleMovieModel.postValue(model)
                    eventChannel.send(Event.AddedToTheWatchlist)
                }
            } else {
                simpleMovieModel.value?.apply {
                    isWatchlist = !isWatchlist
                    detailRepository.addSimpleMovie(this)
                    if (isWatchlist)
                        eventChannel.send(Event.AddedToTheWatchlist)
                    else
                        eventChannel.send(Event.RemovedFromTheWatchlist)

                }
            }
        }
    }

    sealed class Event {
        object AddedToTheFavorites : Event()
        object AddedToTheWatchlist : Event()
        object RemovedFromTheFavorites : Event()
        object RemovedFromTheWatchlist : Event()
    }
}