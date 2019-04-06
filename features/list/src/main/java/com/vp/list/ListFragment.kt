package com.vp.list

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle

import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewAnimator
import androidx.lifecycle.*

import com.vp.detail.DetailActivity
import com.vp.list.viewmodel.SearchResult
import com.vp.list.viewmodel.ListViewModel

import javax.inject.Inject

import com.vp.list.viewmodel.ListState
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(), GridPagingScrollListener.LoadMoreItemsListener, ListAdapter.OnItemClickListener {


    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var listViewModel: ListViewModel
    private lateinit var gridPagingScrollListener: GridPagingScrollListener
    private lateinit var listAdapter: ListAdapter
    private var currentQuery = "Interview"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        listViewModel = ViewModelProviders.of(this, factory).get(ListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        if (savedInstanceState != null) {
            val retrieved = savedInstanceState.getString(CURRENT_QUERY)
            retrieved.let { currentQuery = it?:currentQuery }
        }

        initBottomNavigation(view)
        initList()

        observe(listViewModel.observeMovies()) { searchResult ->
            if (searchResult != null) {
                handleResult(listAdapter, searchResult)
            }
        }

        listViewModel.searchMoviesByTitle(currentQuery, 1, true)
        showProgressBar()
    }

    private fun setClickListeners() {

        swipeRefresh.setOnRefreshListener {
            submitSearchQuery(currentQuery)
        }
    }

    private fun initBottomNavigation(view: View) {
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.favorites) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("app://movies/favorites"))
                intent.setPackage(requireContext().packageName)
                startActivity(intent)
            }
            true
        }
    }

    private fun initList() {
        listAdapter = ListAdapter()
        listAdapter.setOnItemClickListener(this)
        recyclerView.adapter = listAdapter
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView.layoutManager = layoutManager

        // Pagination
        gridPagingScrollListener = GridPagingScrollListener(layoutManager)
        gridPagingScrollListener.setLoadMoreItemsListener(this)
        recyclerView.addOnScrollListener(gridPagingScrollListener)
    }

    private fun showProgressBar() {
        viewAnimator.displayedChild = viewAnimator.indexOfChild(progressBar)
    }

    private fun showList() {
        swipeRefresh.isRefreshing = false
        viewAnimator.displayedChild = viewAnimator.indexOfChild(recyclerView)
    }

    private fun showError() {
        swipeRefresh.isRefreshing = false
        viewAnimator.displayedChild = viewAnimator.indexOfChild(errorText)
    }

    private fun handleResult(listAdapter: ListAdapter, searchResult: SearchResult) {
        when (searchResult.listState) {
            ListState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            ListState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
        gridPagingScrollListener.markLoading(false)
    }

    private fun setItemsData(listAdapter: ListAdapter, searchResult: SearchResult) {
        listAdapter.setItems(searchResult.items)

        if (searchResult.totalResult <= listAdapter.itemCount) {
            gridPagingScrollListener.markLastPage(true)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CURRENT_QUERY, currentQuery)
    }

    override fun loadMoreItems(page: Int) {
        gridPagingScrollListener.markLoading(true)
        listViewModel.searchMoviesByTitle(currentQuery, page, false)
    }

    fun submitSearchQuery(query: String) {
        currentQuery = query
        listAdapter.clearItems()
        listViewModel.searchMoviesByTitle(query, 1, true)
        showProgressBar()
    }

    override fun onItemClick(imdbID: String) {
        val detailIntent = Intent(activity, DetailActivity::class.java)
        //replaced the empty spaces to avoid api failure and bad movie ID generated.
        val uri = Uri.parse("").buildUpon()
                .appendQueryParameter("imdbID", imdbID.replace(" ", ""))
                .build()
        detailIntent.data = uri
        startActivity(detailIntent)
    }

    companion object {
        val TAG = "ListFragment"
        private val CURRENT_QUERY = "current_query"
    }
}
