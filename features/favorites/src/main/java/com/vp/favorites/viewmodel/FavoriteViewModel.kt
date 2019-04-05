package com.vp.favorites.viewmodel

import com.vp.database.dao.MoviesDAO
import com.vp.database.model.entity.ListItem

import javax.inject.Inject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteViewModel @Inject constructor() : ViewModel() {
    private val liveData = MutableLiveData<DataBaseResult>()

    private val moviesDAO = MoviesDAO()

    fun observeMovies(): LiveData<DataBaseResult> {
        return liveData
    }

    fun loadFavorites() {

        liveData.value = DataBaseResult.inProgress()

        val results = moviesDAO.getFavoritesMovies()
        if (results != null && results.size > 0) {
            liveData.setValue(DataBaseResult.success(results, results.size))
        } else {
            liveData.setValue(DataBaseResult.error())
        }
    }
}
