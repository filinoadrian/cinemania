package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.google.gson.annotations.SerializedName

data class NetworkEpisodeEntity(
    @SerializedName("id") val id: String,
    @SerializedName("episode_number") val episodeNumber: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("still_path") val stillPath: String?
)