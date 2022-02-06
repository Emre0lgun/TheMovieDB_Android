package com.pointo.movies.ui.search

import androidx.annotation.MainThread
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.pointo.movies.R
import com.pointo.movies.data.models.Movie
import com.pointo.movies.data.models.QueryAndIndex
import com.pointo.movies.data.responses.SearchResponse
import com.pointo.movies.util.Constants.STRING_EMPTY
import com.pointo.movies.util.plusAssign
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    BindingViewModel() {

    val movieListResponse: LiveData<SearchResponse>
    var movieListLiveData: MutableLiveData<MutableList<Movie>> =
        MutableLiveData<MutableList<Movie>>()

    var lastQuery: String = STRING_EMPTY

    val queryAndPageData: MutableStateFlow<QueryAndIndex> =
        MutableStateFlow(QueryAndIndex(STRING_EMPTY, 1))

    @get:Bindable
    var isLoading: Boolean by bindingProperty(false)
        private set

    @get:Bindable
    var isError: Boolean by bindingProperty(true)
        private set

    @get:Bindable
    var errorText: Int by bindingProperty(R.string.error_text_empty)
        private set

    @get:Bindable
    var toast: String? by bindingProperty(null)
        private set

    init {
        movieListResponse = queryAndPageData.filter { it.query.length > 2 }.asLiveData()
            .switchMap { queryAndIndex ->
                searchRepository.searchMovies(
                    queryAndIndex.query,
                    page = queryAndIndex.index,
                    onStart = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { toast = it }
                ).asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
            }

        viewModelScope.launch {
            movieListResponse.asFlow().collect {
                if (it.results.isNullOrEmpty()) {
                    if (it.status_message != null) {
                        toast = it.status_message.toString()
                        notifyPropertyChanged(::toast)
                    } else {
                        errorText = R.string.error_no_movie_found
                        isError = true
                    }
                    isLoading = false
                } else {
                    if (lastQuery.length > 2) {
                        isError = false
                        if (lastQuery == queryAndPageData.value.query) {
                            movieListLiveData += it.results
                        } else {
                            movieListLiveData.value =
                                it.results as MutableList<Movie>?
                        }
                    }
                }
            }
        }
    }

    fun isOnLast(): Boolean {
        return movieListResponse.value?.page == movieListResponse.value?.total_pages
    }

    @MainThread
    fun fetchSearchedMovies() {
        if (!isLoading) {
            queryAndPageData.value =
                QueryAndIndex(queryAndPageData.value.query, queryAndPageData.value.index + 1)
        }
    }

    @MainThread
    fun onSearchKeywordChanged(query: String?) {
        query?.let {
            lastQuery = query
            if (it.length > 2) {
                isError = false
            } else {
                isError = true
                errorText = R.string.error_text_empty
            }
            queryAndPageData.value = (QueryAndIndex(query, 1))
        }

    }
}