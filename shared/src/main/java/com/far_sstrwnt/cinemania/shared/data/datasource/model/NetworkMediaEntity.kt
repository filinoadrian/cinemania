package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.google.gson.annotations.SerializedName

data class NetworkMediaEntity (
    @SerializedName("id") val id: String,
    @SerializedName("poster_path") val posterPath: String?
)