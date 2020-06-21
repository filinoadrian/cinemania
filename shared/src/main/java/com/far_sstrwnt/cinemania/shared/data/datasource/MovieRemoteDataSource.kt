package com.far_sstrwnt.cinemania.shared.data.datasource

import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
        private val service: TmdbService
) {

    suspend fun getSearchMovie(query: String, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.searchMovie(query, page)
    }

    suspend fun getDiscoverMovie(page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.discoverMovie(page)
    }
}