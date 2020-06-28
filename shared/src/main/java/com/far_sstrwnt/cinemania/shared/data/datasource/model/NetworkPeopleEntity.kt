package com.far_sstrwnt.cinemania.shared.data.datasource.model

import com.google.gson.annotations.SerializedName

data class NetworkPeopleEntity(
    @field:SerializedName("id") val id: String?,
    @field:SerializedName("popularity") val popularity: Double,
    @field:SerializedName("known_for_department") val knownForDepartment: String?,
    @field:SerializedName("birthday") val birthday: String?,
    @field:SerializedName("deathday") val deathday: String?,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("gender") val gender: Int?,
    @field:SerializedName("biography") val biography: String?,
    @field:SerializedName("place_of_birth") val placeOfBirth: String?,
    @field:SerializedName("profile_path") val profilePath: String?,
    @field:SerializedName("adult") val adult: Boolean,
    @field:SerializedName("imdb_id") val imdbId: String?,
    @field:SerializedName("homepage") val homepage: String?
)