package com.pointo.movies.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import com.pointo.movies.data.models.Movie
import com.pointo.movies.util.Constants.baseImageUrl


object ViewBinding {

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, text: String?) {
        if (!text.isNullOrEmpty()) {
            Toast.makeText(view.context, text, Toast.LENGTH_SHORT).show()
        }
    }


    @JvmStatic
    @BindingAdapter("imageUrl", "placeHolderDrawable", requireAll = false)
    fun loadPoster(view: ImageView, imageUrl: String?, @DrawableRes placeHolderDrawable: Int?) {
        view.load(baseImageUrl + imageUrl) {
            placeHolderDrawable?.let {
                placeholder(it)
                error(it)
                fallback(it)
            }
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
