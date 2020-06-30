package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkTvEntity

fun NetworkTvEntity.asDomainModel(): TvEntity {
    return TvEntity(
        id = id,
        popularity = popularity,
        voteCount = voteCount,
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalName = originalName,
        name = name,
        voteAverage = voteAverage,
        overview = overview,
        firstAirDate = firstAirDate
    )
}
