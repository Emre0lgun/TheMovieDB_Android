package com.pointo.movies.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pointo.movies.data.models.Movie
import com.pointo.movies.ui.adapters.MovieTitleAdapter
import com.pointo.movies.ui.adapters.MovieWithPosterAdapter

object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("adapter")
    fun bindAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.adapter = adapter.apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        }
    }

    @JvmStatic
    @BindingAdapter("adapterMovieList")
    fun bindAdapterMovieList(view: RecyclerView, movieList: List<Movie>?) {
        if (!movieList.isNullOrEmpty()) {
            (view.adapter as MovieTitleAdapter).setMovieList(movieList)
        }
    }

    @JvmStatic
    @BindingAdapter("adapterSimpleMovieList")
    fun bindAdapterSimpleMovieList(view: RecyclerView, movieList: List<Movie>?) {
        if (!movieList.isNullOrEmpty()) {
            (view.adapter as MovieWithPosterAdapter).setMovieList(movieList)
        }
    }
}
