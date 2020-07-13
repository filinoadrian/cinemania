package com.far_sstrwnt.cinemania.shared.data.datasource.tv

import com.far_sstrwnt.cinemania.shared.data.datasource.api.ResultsResponse
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkCastEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkGenreEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkTvEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkVideoEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class TvRemoteDataSource @Inject constructor(
        private val service: TmdbService
) {
    suspend fun tvGenre(): Result<List<NetworkGenreEntity>> {
        return try {
            Result.Success(service.getTvGenre().genres)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun tvSearch(query: String, page: Int): ResultsResponse<NetworkTvEntity> {
        return service.getTvSearch(query, page)
    }

    suspend fun tvDiscover(genre: String?, page: Int): ResultsResponse<NetworkTvEntity> {
        return service.getTvDiscover(genre, page)
    }

    suspend fun tvSimilar(id: String, page: Int): ResultsResponse<NetworkTvEntity> {
        return service.getTvSimilar(id, page)
    }

    suspend fun tvDetail(id: String): Result<NetworkTvEntity> {
        return try {
            Result.Success(service.getTvDetail(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun tvCast(id: String): Result<List<NetworkCastEntity>> {
        return try {
            Result.Success(service.getTvCredit(id).cast)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun tvVideo(id: String): Result<List<NetworkVideoEntity>> {
        return try {
            Result.Success(service.getTvVideos(id).results)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}