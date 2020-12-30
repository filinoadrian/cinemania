package com.far_sstrwnt.cinemania.model

data class EpisodeEntity(
    val id: String,
    val episodeNumber: Int,
    val name: String,
    val overview: String,
    val stillPath: String?
)