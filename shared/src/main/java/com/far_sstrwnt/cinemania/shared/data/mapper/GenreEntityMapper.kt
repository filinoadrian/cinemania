package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkGenreEntity

fun NetworkGenreEntity.asDomainModel(): GenreEntity {
    return GenreEntity(
        id = id,
        name = name
    )
}