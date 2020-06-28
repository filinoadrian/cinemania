package com.far_sstrwnt.cinemania.shared.data.datasource.api

import com.google.gson.annotations.SerializedName

data class NetworkMovieEntity(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("popularity") val popularity: Double,
    @field:SerializedName("vote_count") val voteCount: Long,
    @field:SerializedName("video") val video: Boolean,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("adult") val adult: Boolean,
    @field:SerializedName("backdrop_path") val backdropPath: String?,
    @field:SerializedName("original_language") val originalLanguage: String,
    @field:SerializedName("original_title") val originalTitle: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("vote_average") val voteAverage: Float,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("release_date") val releaseDate: String?
)