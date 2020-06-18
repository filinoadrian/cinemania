package com.far_sstrwnt.cinemania.api

import com.far_sstrwnt.cinemania.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<Movie> = emptyList()
)