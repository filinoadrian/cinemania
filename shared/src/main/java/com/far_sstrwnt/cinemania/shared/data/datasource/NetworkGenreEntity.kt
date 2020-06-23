package com.far_sstrwnt.cinemania.shared.data.datasource

import com.google.gson.annotations.SerializedName

data class NetworkGenreEntity(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String
)