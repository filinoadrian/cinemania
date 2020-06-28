package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.api.NetworkMovieEntity

fun NetworkMovieEntity.asDomainModel(): MovieEntity {
    return MovieEntity(
        id = id,
        popularity = popularity,
        voteCount = voteCount,
        video = video,
        posterPath = posterPath,
        adult = adult,
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        title = title,
        voteAverage = voteAverage,
        overview = overview,
        releaseDate = releaseDate
    )
}
