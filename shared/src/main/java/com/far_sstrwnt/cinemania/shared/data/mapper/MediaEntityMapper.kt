package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkMediaEntity

fun NetworkMediaEntity.asDomainModel(): MediaEntity {
    return MediaEntity(
        id = id,
        posterPath = posterPath
    )
}