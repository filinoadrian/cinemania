package com.far_sstrwnt.cinemania.shared.data.datasource

import com.google.gson.annotations.SerializedName

data class ResultsResponse<T>(
    @SerializedName("page") val page: Int,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<T> = emptyList()
)