package com.far_sstrwnt.cinemania.shared.data.datasource.api

import com.far_sstrwnt.cinemania.shared.data.datasource.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    // https://api.themoviedb.org/3/discover/movie?api_key=a1f3faf95d3a6c30d3e3b20acfcdbeae&page=1

    // MOVIE
    @GET("genre/movie/list")
    suspend fun getMovieGenre(): GenresResponse

    @GET("discover/movie")
    suspend fun getMovieDiscover(
        @Query("with_genres") genre: String?,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>

    @GET("search/movie")
    suspend fun getMovieSearch(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: String
    ): NetworkMovieEntity

    @GET("movie/{id}/credits")
    suspend fun getMovieCredit(
        @Path("id") id: String
    ): CreditResponse<NetworkCastEntity>

    @GET("movie/{id}/similar")
    suspend fun getMovieSimilar(
        @Path("id") id: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") id: String
    ): VideosResponse

    // TV
    @GET("genre/tv/list")
    suspend fun getTvGenre(): GenresResponse

    @GET("discover/tv")
    suspend fun getTvDiscover(
        @Query("with_genres") genre: String?,
        @Query("page") page: Int
    ): ResultsResponse<NetworkTvEntity>

    @GET("tv/{id}")
    suspend fun getTvDetail(
        @Path("id") id: String
    ): NetworkTvEntity

    @GET("tv/{id}/credits")
    suspend fun getTvCredit(
        @Path("id") id: String
    ): CreditResponse<NetworkCastEntity>

    @GET("tv/{id}/similar")
    suspend fun getTvSimilar(
        @Path("id") id: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkTvEntity>

    @GET("tv/{id}/videos")
    suspend fun getTvVideos(
        @Path("id") id: String
    ): VideosResponse

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