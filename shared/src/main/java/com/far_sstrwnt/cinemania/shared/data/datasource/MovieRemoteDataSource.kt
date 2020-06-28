package com.far_sstrwnt.cinemania.shared.data.datasource

import com.far_sstrwnt.cinemania.shared.data.datasource.api.*
import com.far_sstrwnt.cinemania.shared.result.Result
import timber.log.Timber
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
        private val service: TmdbService
) {
    suspend fun movieGenre(): Result<List<NetworkGenreEntity>> {
        return try {
            Result.Success(service.getMovieGenre().genres)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun movieSearch(query: String, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieSearch(query, page)
    }

    suspend fun movieDiscover(genre: String?, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieDiscover(genre, page)
    }

    suspend fun movieDetail(id: String): Result<NetworkMovieEntity> {
        return try {
            Result.Success(service.getMovieDetail(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun movieCast(id: String): Result<List<NetworkCastEntity>> {
        return try {
            Result.Success(service.getMovieCredit(id).cast)
        } catch (e: Exception) {
            Timber.e(e.message)
            Result.Error(e)
        }
    }
}