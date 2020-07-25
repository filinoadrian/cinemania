package com.far_sstrwnt.cinemania.shared.data.datasource.movie

import com.far_sstrwnt.cinemania.shared.data.datasource.api.*
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkCastEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkGenreEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkMovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkVideoEntity
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

    suspend fun movieSimilar(id: String, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieSimilar(id, page)
    }

    suspend fun movieNowPlaying(page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieNowPlaying(page)
    }

    suspend fun movieUpcoming(page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieUpcoming(page)
    }

    suspend fun moviePopular(page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMoviePopular(page)
    }

    suspend fun movieTopRated(page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieTopRated(page)
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
            Result.Error(e)
        }
    }

    suspend fun movieVideo(id: String): Result<List<NetworkVideoEntity>> {
        return try {
            Result.Success(service.getMovieVideos(id).results)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}