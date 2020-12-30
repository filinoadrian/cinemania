package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.far_sstrwnt.cinemania.model.GenreEntity
import com.google.gson.annotations.SerializedName

data class NetworkMediaEntity (
    @SerializedName(value = "id") val id: String,
    @SerializedName(value = "poster_path", alternate = ["profile_path"]) val posterPath: String?,
    @SerializedName(value = "name", alternate = ["title"]) val name: String,
    @SerializedName(value = "vote_average") val voteAverage: Float?,
    @SerializedName(value = "genre_ids") val genreIds: List<String>?,
    @SerializedName(value = "backdrop_path") val backdropPath: String?,
    @SerializedName(value = "release_date", alternate = ["first_air_date"]) val releaseDate: String?,
    @SerializedName(value = "overview") val overview: String?,
    @SerializedName(value = "genres") val genres: List<GenreEntity>?,
    @SerializedName(value = "number_of_seasons") val numberOfSeasons: Int?
)