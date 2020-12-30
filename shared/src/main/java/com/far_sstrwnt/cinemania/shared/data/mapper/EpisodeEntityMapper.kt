package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.EpisodeEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkEpisodeEntity

fun NetworkEpisodeEntity.asDomainModel(): EpisodeEntity {
    return EpisodeEntity(
        id = id,
        episodeNumber = episodeNumber,
        name = name,
        overview = overview,
        stillPath = stillPath
    )
}