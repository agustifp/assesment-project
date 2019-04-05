package com.vp.list.viewmodel

import com.vp.database.model.entity.ListItem


import java.util.Collections
import java.util.Objects

class SearchResult constructor(val items: List<ListItem>, val totalResult: Int, val listState: ListState) {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as SearchResult?
        return totalResult == that!!.totalResult &&
                items == that.items &&
                listState == that.listState
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }

    companion object {

        fun error(): SearchResult {
            return SearchResult(emptyList(), 0, ListState.ERROR)
        }

        fun success(items: List<ListItem>, totalResult: Int): SearchResult {
            return SearchResult(items, totalResult, ListState.LOADED)
        }

        fun inProgress(): SearchResult {
            return SearchResult(emptyList(), 0, ListState.IN_PROGRESS)
        }
    }
}
