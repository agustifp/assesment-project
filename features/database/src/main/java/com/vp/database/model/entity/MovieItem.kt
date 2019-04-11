package com.vp.database.model.entity

import com.google.gson.annotations.SerializedName

data class MovieItem(var imdbID: String = "",
                     @SerializedName("Title") var title: String = "",
                     @SerializedName("Year") var year: String = "",
                     @SerializedName("Poster") var poster: String = "",
                     @SerializedName("Runtime") var runtime: String = "",
                     @SerializedName("Director") var director: String = "",
                     @SerializedName("Plot") var plot: String = "",
                     var isFavorite: Boolean = false)
