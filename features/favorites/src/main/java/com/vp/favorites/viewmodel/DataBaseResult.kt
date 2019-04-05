package com.vp.favorites.viewmodel

import com.vp.database.model.entity.ListItem

import java.util.Objects

class DataBaseResult constructor(val items: List<ListItem>, private val totalResult: Int, val listState: FavoriteState) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DataBaseResult?
        return totalResult == that!!.totalResult &&
                items == that.items &&
                listState == that.listState
    }

    override fun hashCode(): Int {
        return Objects.hash(items, totalResult, listState)
    }

    companion object {

        fun error(): DataBaseResult {
            return DataBaseResult(emptyList(), 0, FavoriteState.ERROR)
        }

        fun success(items: List<ListItem>, totalResult: Int): DataBaseResult {
            return DataBaseResult(items, totalResult, FavoriteState.LOADED)
        }

        fun inProgress(): DataBaseResult {
            return DataBaseResult(emptyList(), 0, FavoriteState.IN_PROGRESS)
        }
    }
}
