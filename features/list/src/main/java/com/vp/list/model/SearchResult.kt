package com.vp.list.model


import com.vp.database.model.entity.MovieItem

sealed class SearchResult {

    companion object {

        fun error(): SearchResult {
            return ErrorResult(emptyList())
        }

        fun success(items: List<MovieItem>, totalResult: Int): SearchResult {
            return LoadedResult(items, totalResult)
        }

        fun inProgress(): SearchResult {
            return InProgressResult(emptyList())
        }
    }
}

data class ErrorResult(val items: List<MovieItem>): SearchResult()
data class LoadedResult(val items: List<MovieItem>,val  totalResult: Int): SearchResult()
data class InProgressResult(val items: List<MovieItem>): SearchResult()
