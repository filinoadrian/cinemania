package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkCastEntity

fun NetworkCastEntity.asDomainModel(): CastEntity {
    return CastEntity(
        castId = castId,
        character = character,
        id = id,
        creditId = creditId,
        gender = gender,
        name = name,
        order = order,
        profilePath = profilePath
    )
}