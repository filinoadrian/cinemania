package com.far_sstrwnt.cinemania.shared.data.datasource.api

import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkVideoEntity
import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<NetworkVideoEntity> = emptyList()
)