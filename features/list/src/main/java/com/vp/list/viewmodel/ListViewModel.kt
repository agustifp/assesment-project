package com.vp.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.vp.database.model.entity.ListItem
import com.vp.list.model.SearchResponse
import com.vp.list.service.SearchService

import java.util.ArrayList

import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel @Inject constructor(private val searchService: SearchService) : ViewModel() {
    private val liveData = MutableLiveData<SearchResult>()

    private var currentTitle = ""
    private val aggregatedItems = arrayListOf<ListItem>()

    fun observeMovies(): LiveData<SearchResult> {
        return liveData
    }

    fun searchMoviesByTitle(title: String, page: Int, hasToReload: Boolean) {

        if (page == 1 && title != currentTitle || hasToReload) {
            aggregatedItems.clear()
            currentTitle = title
            liveData.value = SearchResult.inProgress()
        }

        searchService.search(title, page).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {

                val result = response.body()

                if (result != null && result.hasResponse()) {
                    aggregatedItems.addAll(result.search)
                    liveData.value = SearchResult.success(aggregatedItems, aggregatedItems.size)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                liveData.value = SearchResult.error()
            }
        })
    }
}
