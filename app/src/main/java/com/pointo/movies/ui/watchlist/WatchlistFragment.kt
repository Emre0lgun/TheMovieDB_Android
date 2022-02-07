package com.pointo.movies.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.pointo.movies.R
import com.pointo.movies.databinding.FragmentWatchlistBinding
import com.pointo.movies.ui.adapters.MovieWithPosterAdapter
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : BindingFragment<FragmentWatchlistBinding>(R.layout.fragment_watchlist) {

    private val vm: WatchlistViewModel by viewModels()

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

    private fun setupUI(binding: FragmentWatchlistBinding) {
        with(binding) {
            lifecycleOwner = this@WatchlistFragment
            viewModel = vm
            adapter = MovieWithPosterAdapter()
            fragmentWatchlistRvMovies.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

}