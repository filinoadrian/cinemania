package com.far_sstrwnt.cinemania.shared.data.datasource.api

import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkGenreEntity
import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres") val genres: List<NetworkGenreEntity> = emptyList()
)