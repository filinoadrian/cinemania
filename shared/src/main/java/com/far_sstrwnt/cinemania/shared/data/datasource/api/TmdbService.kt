package com.far_sstrwnt.cinemania.shared.data.datasource.api

import com.far_sstrwnt.cinemania.shared.data.datasource.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    // https://api.themoviedb.org/3/discover/movie?api_key=<api_key>&page=1

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

    @GET("{media_type}/{id}")
    suspend fun getMediaDetail(
        @Path("media_type") mediaType: String,
        @Path("id") id: String
    ): NetworkMediaEntity

    @GET("{media_type}/{id}/similar")
    suspend fun getMediaSimilar(
        @Path("media_type") mediaType: String,
        @Path("id") id: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMediaEntity>

    @GET("{media_type}/{id}/videos")
    suspend fun getMediaVideos(
        @Path("media_type") mediaType: String,
        @Path("id") id: String
    ): VideosResponse

    @GET("{media_type}/{id}/credits")
    suspend fun getMediaCredits(
        @Path("media_type") mediaType: String,
        @Path("id") id: String
    ): CreditResponse<NetworkCastEntity>

    @GET("tv/{id}/season/{season_number}")
    suspend fun getTvSeason(
        @Path("id") id: String,
        @Path("season_number") seasonNumber: Int
    ): SeasonResponse
}