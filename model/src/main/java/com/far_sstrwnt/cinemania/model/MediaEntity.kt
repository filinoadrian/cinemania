package com.far_sstrwnt.cinemania.model

data class MediaEntity(
    val id: String,
    val posterPath: String?
)

enum class MediaType(val value: String) {
    MOVIE("movie"),
    TV("tv"),
    PEOPLE("people")
}

enum class MediaCategory(val value: String) {
    NOW_PLAYING("now_playing"),
    UPCOMING("upcoming"),
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    AIRING_TODAY("airing_today"),
    ON_THE_AIR("on_the_air")
}