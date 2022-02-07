package com.pointo.movies.ui.favorite

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.pointo.movies.R
import com.pointo.movies.data.models.Movie
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) :
    BindingViewModel() {

    val movieListResponse: LiveData<List<Movie>>
    var movieListLiveData: MutableLiveData<MutableList<Movie>> =
        MutableLiveData<MutableList<Movie>>()

    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var errorText: Int by bindingProperty(R.string.error_text_empty)
        private set

    @get:Bindable
    var isError: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var toast: String? by bindingProperty(null)
        private set

    init {
        movieListResponse = favoriteRepository.getFavorites(
            onStart = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { toast = it }
        ).asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

        viewModelScope.launch(Dispatchers.IO) {
            movieListResponse.asFlow().collect {
                if (it.isNullOrEmpty()) {
                    errorText = R.string.error_no_movie_found_in_favorites
                    isError = true
                } else {
                    isError = false
                    movieListLiveData.postValue(it as MutableList<Movie>)
                }
            }
        }
    }

}