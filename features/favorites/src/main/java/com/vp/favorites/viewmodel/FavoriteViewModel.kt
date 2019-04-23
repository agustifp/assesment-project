package com.vp.favorites.viewmodel

import com.vp.database.dao.MoviesDAO

import javax.inject.Inject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.model.SearchResult

class FavoriteViewModel @Inject constructor() : ViewModel() {
    private val liveData = MutableLiveData<com.example.data.model.SearchResult>()

    fun observeMovies(): LiveData<com.example.data.model.SearchResult> {
        return liveData
    }

    fun loadFavorites() {

        liveData.value = com.example.data.model.SearchResult.inProgress()

        val results = MoviesDAO.getFavoritesMovies()
        if (results != null && results.isNotEmpty()) {
            liveData.setValue(com.example.data.model.SearchResult.success(results, results.size))
        } else {
            liveData.setValue(com.example.data.model.SearchResult.error())
        }
    }
}
