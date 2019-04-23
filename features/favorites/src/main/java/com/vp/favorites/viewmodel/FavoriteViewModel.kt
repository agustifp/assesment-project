package com.vp.favorites.viewmodel

import com.vp.database.dao.MoviesDAO

import javax.inject.Inject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.list.model.SearchResult

class FavoriteViewModel @Inject constructor() : ViewModel() {
    private val liveData = MutableLiveData<SearchResult>()

    fun observeMovies(): LiveData<SearchResult> {
        return liveData
    }

    fun loadFavorites() {

        liveData.value = SearchResult.inProgress()

        val results = MoviesDAO.getFavoritesMovies()
        if (results != null && results.isNotEmpty()) {
            liveData.setValue(SearchResult.success(results, results.size))
        } else {
            liveData.setValue(SearchResult.error())
        }
    }
}
