package com.vp.database.model.entity

import com.google.gson.annotations.SerializedName

class ListItem(@SerializedName("Title")
               var title: String = "",
               @SerializedName("Year")
               var year: String = "",
               var imdbID: String = "",
               @SerializedName("Poster")
               var poster: String = "")
