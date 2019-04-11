package com.vp.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.database.dao.MoviesDAO
import com.vp.database.model.entity.MovieItem
import com.vp.detail.service.DetailService
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

class DetailsViewModel @Inject constructor(private val detailService: DetailService) : ViewModel() {

    private val details: MutableLiveData<MovieItem> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    lateinit var movieId: String

    fun title(): LiveData<String> = title

    fun details(): LiveData<MovieItem> = details

    fun state(): LiveData<LoadingState> = loadingState

    fun fetchDetails(movieIdIntent: String) {
        movieId = movieIdIntent
        loadingState.value = LoadingState.IN_PROGRESS
        detailService.getMovie(movieId).enqueue(object : Callback, retrofit2.Callback<MovieItem> {
            override fun onResponse(call: Call<MovieItem>?, response: Response<MovieItem>?) {
                details.postValue(response?.body())

                response?.body()?.title?.let {
                    title.postValue(it)
                }

                loadingState.value = LoadingState.LOADED
            }

            override fun onFailure(call: Call<MovieItem>?, t: Throwable?) {
                details.postValue(null)
                loadingState.value = LoadingState.ERROR
            }
        })
    }

    fun saveToFavorite() =
            MoviesDAO.saveFavorite(MovieItem().apply {
                imdbID = movieId
                poster = details.value?.poster ?: ""
                title = details.value?.title ?: ""
                year = details.value?.year ?: ""
                director = details.value?.director ?: ""
                plot = details.value?.plot ?: ""
                runtime = details.value?.plot ?: ""
            })


    fun removeFavorite() =
            MoviesDAO.removeFavorite(movieId)


    fun checkFavorite(): Boolean = MoviesDAO.isFavorite(movieId)

    enum class LoadingState {
        IN_PROGRESS, LOADED, ERROR
    }
}