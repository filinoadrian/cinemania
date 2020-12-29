package com.far_sstrwnt.cinemania.shared.data.datasource.api

import com.far_sstrwnt.cinemania.shared.data.datasource.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    // https://api.themoviedb.org/3/discover/movie?api_key=a1f3faf95d3a6c30d3e3b20acfcdbeae&page=1

    @GET("trending/{media_type}/day")
    suspend fun getMediaTrending(
        @Path("media_type") mediaType: String
    ): ResultsResponse<NetworkMediaEntity>

    @GET("genre/{media_type}/list")
    suspend fun getMediaGenre(
        @Path("media_type") mediaType: String
    ): GenresResponse

    @GET("{media_type}/{category}")
    suspend fun getMediaByCategory(
        @Path("media_type") mediaType: String,
        @Path("category") category: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMediaEntity>

    @GET("{action}/{media_type}")
    suspend fun getMediaByAction(
        @Path("action") action: String,
        @Path("media_type") mediaType: String,
        @Query("with_genres") genre: String?,
        @Query("query") query: String?,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMediaEntity>

    @GET("{entity}/{id}/credits")
    suspend fun getCredit(
        @Path("entity") entity: String,
        @Path("id") id: String
    ): CreditResponse<NetworkCastEntity>

    @GET("{entity}/{id}/videos")
    suspend fun getVideo(
        @Path("entity") entity: String,
        @Path("id") id: String
    ): VideosResponse

    // MOVIE
    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: String
    ): NetworkMovieEntity

    @GET("movie/{id}/similar")
    suspend fun getMovieSimilar(
        @Path("id") id: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>

    // TV
    @GET("tv/{id}")
    suspend fun getTvDetail(
        @Path("id") id: String
    ): NetworkTvEntity

    @GET("tv/{id}/similar")
    suspend fun getTvSimilar(
        @Path("id") id: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkTvEntity>

    // PEOPLE
    @GET("person/{id}")
    suspend fun getPeopleDetail(
        @Path("id") id: String
    ): NetworkPeopleEntity

    @GET("person/{id}/movie_credits")
    suspend fun getPeopleMovieCredit(
        @Path("id") id: String
    ): CreditResponse<NetworkMovieEntity>

    @GET("person/{id}/tv_credits")
    suspend fun getPeopleTvCredit(
        @Path("id") id: String
    ): CreditResponse<NetworkTvEntity>
}