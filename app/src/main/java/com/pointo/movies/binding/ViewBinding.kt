package com.pointo.movies.binding

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.pointo.movies.data.models.Movie


object ViewBinding {

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, text: String?) {
        if (!text.isNullOrEmpty()) {
            Toast.makeText(view.context, text, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun bindGone(view: View, shouldBeGone: Boolean) {
        view.visibility = if (shouldBeGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("title")
    fun bindTitle(view: TextView, movie: Movie) {
        if (movie.release_date.isNullOrBlank()) {
            view.text = movie.title
        } else {
            view.text = "${movie.title} - ${movie.release_date.substring(0, 4)}"
        }
    }
}
