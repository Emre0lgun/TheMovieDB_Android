package com.pointo.movies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.pointo.movies.R
import com.pointo.movies.binding.ViewBinding.bindGone
import com.pointo.movies.databinding.FragmentSearchBinding
import com.pointo.movies.ui.adapters.MovieTitleAdapter
import com.pointo.movies.util.*
import com.skydoves.bindables.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val vm: SearchViewModel by viewModels()

    private var previousQuery = Constants.STRING_EMPTY

    private var delay: Long = 600

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

    private fun setupUI(binding: FragmentSearchBinding) {
        with(binding) {
            lifecycleOwner = this@SearchFragment
            viewModel = vm
            adapter = MovieTitleAdapter()
            fragmentSearchRvMovies.let {
                it.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                it.setHasFixedSize(true)
                it.addOnScrollListener(PaginationScrollListener(
                    isLoading = { vm.isLoading },
                    loadMore = { vm.fetchSearchedMovies() },
                    onLast = { vm.isOnLast() }
                ))
            }

            fragmentSearchIvClearIcon.setOnClickListener {
                fragmentSearchEtSearchView.text = null
                (fragmentSearchRvMovies.adapter as MovieTitleAdapter).clearItems()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (vm.lastQuery.isNotEmpty()) {
            binding.fragmentSearchIvClearIcon.show()
        }
        vm.queryAndPageData.asLiveData().observe(viewLifecycleOwner) {
            if (it.query != previousQuery) {
                previousQuery = it.query
                binding.adapter?.clearItems()
                vm.movieListLiveData.value?.clear()
            }
        }

        binding.fragmentSearchEtSearchView.textChanges()
            .debounce(delay)
            .onEach {
                vm.onSearchKeywordChanged(it.toString())
                bindGone(binding.fragmentSearchIvClearIcon, it.isNullOrBlank())
            }
            .launchIn(lifecycleScope)

    }
}