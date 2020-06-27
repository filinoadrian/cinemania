package com.far_sstrwnt.cinemania.shared.data.datasource

import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
        private val service: TmdbService
) {
    suspend fun genreMovie(): Result<List<NetworkGenreEntity>> {
        return try {
            Result.Success(service.getGenreMovie().genres)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun searchMovie(query: String, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getSearchMovie(query, page)
    }

    suspend fun discoverMovie(genre: String?, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getDiscoverMovie(genre, page)
    }

    suspend fun detailMovie(id: String): Result<NetworkMovieEntity> {
        return try {
            Result.Success(service.getMovieDetail(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}