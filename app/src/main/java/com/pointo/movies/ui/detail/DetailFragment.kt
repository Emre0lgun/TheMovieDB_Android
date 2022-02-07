package com.pointo.movies.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pointo.movies.R
import com.pointo.movies.databinding.FragmentDetailBinding
import com.pointo.movies.util.observeInLifecycle
import com.pointo.movies.util.shortToast
import com.pointo.movies.util.toMovieDetailModel
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val vm: DetailViewModel by viewModels()

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding {
            lifecycleOwner = this@DetailFragment
            viewModel = vm
            movie = args.movie.toMovieDetailModel()
            fragmentDetailToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let { shortToast(it) }
        }

        vm.simpleMovieModel.observe(viewLifecycleOwner) { model ->
            model?.let {
                if (it.isFavorite) {
                    setMenuItemIcon(0, R.drawable.ic_favorite_selected)
                } else {
                    setMenuItemIcon(0, R.drawable.ic_favorite_not_selected)
                }
                if (it.isWatchlist) {
                    setMenuItemIcon(1, R.drawable.ic_watchlist_selected)
                } else {
                    setMenuItemIcon(1, R.drawable.ic_watchlist_not_selected)
                }
            }
        }

        vm.eventsFlow.onEach {
            when (it) {
                DetailViewModel.Event.AddedToTheFavorites -> {
                    shortToast(getString(R.string.detail_toast_add_favorite))
                }
                DetailViewModel.Event.AddedToTheWatchlist -> {
                    shortToast(getString(R.string.detail_toast_add_watchlist))
                }
                DetailViewModel.Event.RemovedFromTheFavorites -> {
                    shortToast(getString(R.string.detail_toast_remove_favorite))
                }
                DetailViewModel.Event.RemovedFromTheWatchlist -> {
                    shortToast(getString(R.string.detail_toast_remove_watchlist))
                }
            }
        }.observeInLifecycle(viewLifecycleOwner)


        binding.fragmentDetailToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.detailMenu_favorite -> {
                    vm.favoriteClicked()
                    true
                }

                R.id.detailMenu_watchlist -> {
                    vm.watchlistClicked()
                    true
                }

                else -> {
                    super.onOptionsItemSelected(item)
                }
            }
        }
    }

    private fun setMenuItemIcon(itemIndex: Int, drawableId: Int) {
        binding.fragmentDetailToolbar.menu.getItem(itemIndex).icon =
            ContextCompat.getDrawable(requireContext(), drawableId)
    }
}