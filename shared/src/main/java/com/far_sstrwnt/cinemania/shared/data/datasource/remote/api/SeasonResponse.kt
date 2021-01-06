package com.far_sstrwnt.cinemania.shared.data.datasource.remote.api

import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkEpisodeEntity
import com.google.gson.annotations.SerializedName

data class SeasonResponse(
    @SerializedName("id") val id: String,
    @SerializedName("episodes") val episodes: List<NetworkEpisodeEntity>
)