package com.far_sstrwnt.cinemania.model

data class CastEntity(
    val castId: String,
    val character: String,
    val creditId: String?,
    val gender: Int?,
    val id: String,
    val name: String,
    val order: Int?,
    val profilePath: String?
)