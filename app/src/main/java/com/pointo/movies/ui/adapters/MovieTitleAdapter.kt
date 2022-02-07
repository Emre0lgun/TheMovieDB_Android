package com.pointo.movies.ui.adapters

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.pointo.movies.R
import com.pointo.movies.data.models.Movie
import com.pointo.movies.databinding.ItemMovieBinding
import com.pointo.movies.ui.search.SearchFragmentDirections
import com.skydoves.bindables.binding

class MovieTitleAdapter : RecyclerView.Adapter<MovieTitleAdapter.MovieViewHolder>() {

    private val items: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            parent.binding<ItemMovieBinding>(R.layout.item_movie)
        return MovieViewHolder(binding).apply {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { pos -> pos != NO_POSITION }
                    ?: return@setOnClickListener
                it.findNavController().navigate(
                    SearchFragmentDirections.actionToDetailFragment(items[position])
                )
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

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.binding.apply {
            movie = items[position]
            executePendingBindings()
        }
    }

    override fun getItemCount() = items.size

    class MovieViewHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root)
}