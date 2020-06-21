package com.far_sstrwnt.cinemania.shared.data.datasource

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultsResponse<NetworkMovieEntity>
}