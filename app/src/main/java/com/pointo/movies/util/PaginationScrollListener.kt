package com.pointo.movies.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationScrollListener(
    private val isLoading: () -> Boolean,
    private val loadMore: (Int) -> Unit,
    private val onLast: () -> Boolean = { true }
) : RecyclerView.OnScrollListener() {

    var currentPage: Int = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        recyclerView.layoutManager?.let {
            val visibleItemCount = it.childCount
            val totalItemCount = it.itemCount
            val lastVisibleItemPosition = (it as LinearLayoutManager).findLastVisibleItemPosition();

            if (onLast() || isLoading()) return

            if ((visibleItemCount + lastVisibleItemPosition) - 2 >= totalItemCount) {
                loadMore(++currentPage)
            }
        }
    }
}
