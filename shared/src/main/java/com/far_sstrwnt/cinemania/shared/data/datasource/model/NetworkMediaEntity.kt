package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.google.gson.annotations.SerializedName

data class NetworkMediaEntity (
    @SerializedName(value = "id") val id: String,
    @SerializedName(value = "poster_path", alternate = ["profile_path"]) val posterPath: String?,
    @SerializedName(value = "name", alternate = ["title"]) val name: String,
    @SerializedName(value = "vote_average") val voteAverage: Float?,
    @SerializedName(value = "genre_ids") val genreIds: List<String>?
)