package com.pointo.movies.ui.adapters

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.pointo.movies.R
import com.pointo.movies.data.models.Movie
import com.pointo.movies.databinding.ItemMovieWithPosterBinding
import com.skydoves.bindables.binding

class MovieWithPosterAdapter :
    RecyclerView.Adapter<MovieWithPosterAdapter.MovieWithPosterViewHolder>() {

    private val items: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieWithPosterViewHolder {
        val binding = parent.binding<ItemMovieWithPosterBinding>(R.layout.item_movie_with_poster)
        return MovieWithPosterViewHolder(binding).apply {
            binding.root.setOnClickListener {
                Toast.makeText(it.context, "To be implemented!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setMovieList(movieList: List<Movie>) {
        val previousItemSize = items.size
        items.clear()
        items.addAll(movieList)
        notifyItemRangeInserted(previousItemSize, items.size)
    }

    fun clearItems() {
        val previousItemSize = items.size
        items.clear()
        notifyItemRangeRemoved(0, previousItemSize)
    }

    override fun onBindViewHolder(holder: MovieWithPosterViewHolder, position: Int) {
        holder.binding.apply {
            movie = items[position]
            executePendingBindings()
        }
    }

    override fun getItemCount() = items.size

    class MovieWithPosterViewHolder(val binding: ItemMovieWithPosterBinding) :
        RecyclerView.ViewHolder(binding.root)
}