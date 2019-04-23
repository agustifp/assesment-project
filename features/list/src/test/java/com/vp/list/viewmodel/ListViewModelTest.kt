package com.vp.list.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer

import com.vp.list.model.SearchResponse
import com.vp.list.model.SearchResult
import com.vp.list.service.SearchService

import org.junit.Rule
import org.junit.Test

import java.io.IOException
import java.util.Objects

import retrofit2.mock.Calls

import org.assertj.core.api.Assertions.assertThat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Call

class ListViewModelTest {
    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @Test
    fun shouldReturnErrorState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.failure<SearchResponse>(IOException()))
        val listViewModel = ListViewModel(searchService)

        //when
        listViewModel.searchMoviesByTitle("title", 1, true)

        //then
        assertThat(listViewModel.observeMovies().value).isEqualTo(SearchResult.error())

    }

    @Test
    fun shouldReturnInProgressState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1, true)

        //then
        verify(mockObserver).onChanged(SearchResult.inProgress())
    }

    @Test
    fun shouldReturnInSuccessState() {
        //given
        val searchService = mock(SearchService::class.java)
        `when`<Call<SearchResponse>>(searchService.search(anyString(), anyInt())).thenReturn(Calls.response(mock(SearchResponse::class.java)))
        val listViewModel = ListViewModel(searchService)
        val mockObserver = mock(Observer::class.java) as Observer<SearchResult>
        listViewModel.observeMovies().observeForever(mockObserver)

        //when
        listViewModel.searchMoviesByTitle("title", 1, true)

        //then
        assertThat(Objects.requireNonNull<SearchResult>(listViewModel.observeMovies().value)).isEqualTo(SearchResult.success(emptyList(), 0))
    }
}