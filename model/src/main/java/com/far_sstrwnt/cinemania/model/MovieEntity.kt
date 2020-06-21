package com.far_sstrwnt.cinemania.model

data class MovieEntity(
    val id: String,
    val popularity: Double,
    val voteCount: Long,
    val video: Boolean,
    val posterPath: String?,
    val adult: Boolean,
    val backdropPath: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val title: String,
    val voteAverage: Float,
    val overview: String,
    val releaseDate: String?
)