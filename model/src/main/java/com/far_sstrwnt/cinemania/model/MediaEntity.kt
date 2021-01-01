package com.far_sstrwnt.cinemania.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_media")
data class MediaEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "poster_path") val posterPath: String?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "original_title") val originalTitle: String?,
    @ColumnInfo(name = "vote_average") val voteAverage: Float?,
    @ColumnInfo(name = "genre_list") val genreList: List<String>?,
    @ColumnInfo(name = "backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "release_date") val releaseDate: String?,
    @ColumnInfo(name = "overview") val overview: String?,
    @ColumnInfo(name = "number_of_seasons") val numberOfSeasons: Int?
)

enum class MediaType(val value: String) {
    MOVIE("movie"),
    TV("tv")
}

enum class MediaCategory(val value: String) {
    NOW_PLAYING("now_playing"),
    UPCOMING("upcoming"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    AIRING_TODAY("airing_today"),
    ON_THE_AIR("on_the_air")
}