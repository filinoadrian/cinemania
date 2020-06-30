package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.google.gson.annotations.SerializedName

data class NetworkTvEntity(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("popularity") val popularity: Double,
    @field:SerializedName("vote_count") val voteCount: Long,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("original_language") val originalLanguage: String,
    @field:SerializedName("original_name") val originalName: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("vote_average") val voteAverage: Float,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("first_air_date") val firstAirDate: String?
)