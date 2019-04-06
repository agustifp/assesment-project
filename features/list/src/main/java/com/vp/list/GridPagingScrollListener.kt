package com.vp.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridPagingScrollListener constructor(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private lateinit var EMPTY_LISTENER: LoadMoreItemsListener
    private lateinit var loadMoreItemsListener: LoadMoreItemsListener

    var isLastPage = false
    var isLoading = false

    private val isNotFirstPage: Boolean
        get() = layoutManager.findFirstVisibleItemPosition() >= 0 && layoutManager.itemCount >= PAGE_SIZE

    fun resetPage() {
        currentPage = 1
    }

    private var currentPage = 1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (shouldLoadNextPage()) {
            currentPage++
            loadMoreItemsListener.loadMoreItems(currentPage)
        }
    }

    private fun shouldLoadNextPage(): Boolean {
        return !isLoading && userScrollsToNextPage() && isNotFirstPage && hasNextPage()
    }

    private fun userScrollsToNextPage(): Boolean {
        return layoutManager.childCount + layoutManager.findFirstVisibleItemPosition() >= layoutManager.itemCount
    }

    private fun hasNextPage(): Boolean {
        return !isLastPage
    }

    fun setLoadMoreItemsListener(loadMoreItemsListener: LoadMoreItemsListener?) {
        if (loadMoreItemsListener != null) {
            this.loadMoreItemsListener = loadMoreItemsListener
        } else {
            this.loadMoreItemsListener = EMPTY_LISTENER
        }
    }

    interface LoadMoreItemsListener {
        fun loadMoreItems(page: Int)
    }

    companion object {
        private val PAGE_SIZE = 10
    }
}
