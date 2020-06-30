package com.far_sstrwnt.cinemania.model

data class TvEntity(
    val id: String,
    val popularity: Double,
    val voteCount: Long,
    val posterPath: String?,
    val backdropPath: String?,
    val originalLanguage: String,
    val originalName: String,
    val name: String,
    val voteAverage: Float,
    val overview: String,
    val firstAirDate: String?
)