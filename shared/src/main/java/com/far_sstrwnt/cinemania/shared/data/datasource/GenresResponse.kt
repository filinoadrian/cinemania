package com.far_sstrwnt.cinemania.shared.data.datasource

import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres") val genres: List<NetworkGenreEntity> = emptyList()
)