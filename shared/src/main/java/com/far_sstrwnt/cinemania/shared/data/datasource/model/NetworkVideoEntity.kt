package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.google.gson.annotations.SerializedName

data class NetworkVideoEntity(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("key") val key: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("site") val site: String,
    @field:SerializedName("size") val size: Int,
    @field:SerializedName("type") val type: String
)