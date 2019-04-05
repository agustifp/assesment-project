package com.vp.favorites

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ViewAnimator

import com.vp.detail.DetailActivity
import com.vp.favorites.viewmodel.DataBaseResult
import com.vp.favorites.viewmodel.FavoriteViewModel

import javax.inject.Inject
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vp.favorites.viewmodel.FavoriteState
import com.vp.list.observe
import dagger.android.support.AndroidSupportInjection

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var listAdapter: FavoriteAdapter? = null
    private var viewAnimator: ViewAnimator? = null
    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var errorTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews(view)

        initList()

        observe(favoriteViewModel.observeMovies()) {
            searchResult ->
            if (searchResult != null) {
                handleResult(listAdapter!!, searchResult)
            }
        }

        favoriteViewModel.loadFavorites()
        showProgressBar()
    }

    private fun setViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        viewAnimator = view.findViewById(R.id.viewAnimator)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorText)

    }


    private fun initList() {
        listAdapter = FavoriteAdapter()
        listAdapter!!.setOnItemClickListener(this)
        recyclerView!!.adapter = listAdapter
        val layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
    }

    private fun showProgressBar() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(errorTextView)
    }

    private fun handleResult(listAdapter: FavoriteAdapter, searchResult: DataBaseResult) {
        when (searchResult.listState) {
            FavoriteState.LOADED -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            FavoriteState.IN_PROGRESS -> {
                showProgressBar()
            }
            else -> {
                showError()
            }
        }
    }

    private fun setItemsData(listAdapter: FavoriteAdapter, searchResult: DataBaseResult) {
        listAdapter.setItems(searchResult.items)


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
        internal val TAG = "FavoriteFragment"
    }
}
