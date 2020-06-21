package com.far_sstrwnt.cinemania.shared.data.datasource

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    // https://api.themoviedb.org/3/discover/movie?api_key=a1f3faf95d3a6c30d3e3b20acfcdbeae&page=1

    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>
}