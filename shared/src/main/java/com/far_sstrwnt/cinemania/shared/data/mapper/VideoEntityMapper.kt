package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkVideoEntity

fun NetworkVideoEntity.asDomainModel(): VideoEntity {
    return VideoEntity(
        id = id,
        key = key,
        name = name,
        site = site,
        size = size,
        type = type
    )
}