package com.far_sstrwnt.cinemania.shared.data.datasource.remote.api

import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkGenreEntity
import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres") val genres: List<NetworkGenreEntity> = emptyList()
)