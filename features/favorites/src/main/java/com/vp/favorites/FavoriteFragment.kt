package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.vp.detail.DetailActivity
import com.vp.favorites.viewmodel.FavoriteViewModel

import javax.inject.Inject
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.list.model.ErrorResult
import com.vp.list.model.InProgressResult
import com.vp.list.model.LoadedResult
import com.vp.list.model.SearchResult
import com.vp.list.observe
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var listAdapter: FavoriteAdapter? = null


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

        initList()

        observe(favoriteViewModel.observeMovies()) {
            searchResult ->
            searchResult?.let { handleResult(listAdapter!!, it) }
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.loadFavorites()
    }

    private fun initList() {
        listAdapter = FavoriteAdapter()
        listAdapter!!.setOnItemClickListener(this)
        recyclerView!!.adapter = listAdapter
        val layoutManager = GridLayoutManager(context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        recyclerView!!.layoutManager = layoutManager
    }

    private fun showProgressBar() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(progressBar)
    }

    private fun showList() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(recyclerView)
    }

    private fun showError() {
        viewAnimator!!.displayedChild = viewAnimator!!.indexOfChild(errorText)
    }

    private fun handleResult(listAdapter: FavoriteAdapter, searchResult: SearchResult) {
        when (searchResult) {
            is LoadedResult -> {
                setItemsData(listAdapter, searchResult)
                showList()
            }
            is InProgressResult -> showProgressBar()
            is ErrorResult -> showError()
        }
    }

    private fun setItemsData(listAdapter: FavoriteAdapter, searchResult: SearchResult) {
        listAdapter.setItems((searchResult as LoadedResult).items)
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
        internal const val TAG = "FavoriteFragment"
    }
}
