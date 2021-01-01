package com.far_sstrwnt.cinemania.shared.data.datasource.remote.api

import com.google.gson.annotations.SerializedName

data class CreditResponse<T>(
    @SerializedName("id") val id: String,
    @SerializedName("cast") val cast: List<T>
)