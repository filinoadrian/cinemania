package com.far_sstrwnt.cinemania.model

data class PeopleEntity(
    val id: String?,
    val popularity: Double,
    val knownForDepartment: String?,
    val birthday: String?,
    val deathday: String?,
    val name: String,
    val gender: Int?,
    val biography: String?,
    val placeOfBirth: String?,
    val profilePath: String?,
    val adult: Boolean,
    val imdbId: String?,
    val homepage: String?
)