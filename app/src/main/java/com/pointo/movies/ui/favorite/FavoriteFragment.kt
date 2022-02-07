package com.pointo.movies.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.pointo.movies.R
import com.pointo.movies.databinding.FragmentFavoriteBinding
import com.pointo.movies.ui.adapters.MovieWithPosterAdapter
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : BindingFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {

    private val vm: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            setupUI(this)
        }.root

    }

    private fun setupUI(binding: FragmentFavoriteBinding) {
        with(binding) {
            lifecycleOwner = this@FavoriteFragment
            viewModel = vm
            adapter = MovieWithPosterAdapter()
            fragmentFavoriteRvMovies.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }
}