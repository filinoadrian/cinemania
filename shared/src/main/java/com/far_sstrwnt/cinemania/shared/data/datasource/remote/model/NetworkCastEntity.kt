package com.far_sstrwnt.cinemania.shared.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class NetworkCastEntity(
    @field:SerializedName("cast_id") val castId: String?,
    @field:SerializedName("character") val character: String,
    @field:SerializedName("credit_id") val creditId: String?,
    @field:SerializedName("gender") val gender: Int?,
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("order") val order: Int?,
    @field:SerializedName("profile_path") val profilePath: String?
)