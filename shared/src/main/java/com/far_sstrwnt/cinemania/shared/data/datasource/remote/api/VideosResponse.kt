package com.far_sstrwnt.cinemania.shared.data.datasource.remote.api

import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkVideoEntity
import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<NetworkVideoEntity> = emptyList()
)