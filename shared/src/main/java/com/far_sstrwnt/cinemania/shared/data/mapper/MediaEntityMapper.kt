package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.NetworkMediaEntity

fun NetworkMediaEntity.asDomainModel(): MediaEntity {

    var genreList: List<String> = emptyList()

    if (genreIds != null) {
        genreList = genreIds.map {
            val name = when (it) {
                "28" -> "Action"
                "12" -> "Adventure"
                "16" -> "Animation"
                "35" -> "Comedy"
                "80" -> "Crime"
                "99" -> "Documentary"
                "18" -> "Drama"
                "10751" -> "Family"
                "14" -> "Fantasy"
                "36" -> "History"
                "27" -> "Horror"
                "10402" -> "Music"
                "9648" -> "Mystery"
                "10749" -> "Romance"
                "878" -> "Science Fiction"
                "10770" -> "TV Movie"
                "53" -> "Thriller"
                "10752" -> "War"
                "37" -> "Western"
                "10759" -> "Action & Adventure"
                "10762" -> "Kids"
                "10763" -> "News"
                "10764" -> "Reality"
                "10765" -> "Sci-Fi & Fantasy"
                "10766" -> "Soap"
                "10767" -> "Talk"
                "10768" -> "War & Politics"
                else -> "Other"
            }
            name
        }
    }

    if (genres != null) {
        genreList = genres.map {
            it.name
        }
    }

    return MediaEntity(
        id = id,
        posterPath = posterPath,
        name = name,
        originalTitle = originalTitle,
        voteAverage = voteAverage,
        genreList = genreList,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        overview = overview,
        numberOfSeasons = numberOfSeasons
    )
}