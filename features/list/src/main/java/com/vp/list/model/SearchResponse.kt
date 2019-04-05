package com.vp.list.model

import com.google.gson.annotations.SerializedName
import com.vp.database.model.entity.ListItem

import java.util.Collections.emptyList

class SearchResponse constructor(@field:SerializedName("Response")
                                         private val response: String) {
    @SerializedName("Search")
    val search: List<ListItem> = listOf()

    val totalResults: Int = 0

    fun hasResponse(): Boolean {
        return POSITIVE_RESPONSE == response
    }

    companion object {
        private val POSITIVE_RESPONSE = "True"
    }
}
