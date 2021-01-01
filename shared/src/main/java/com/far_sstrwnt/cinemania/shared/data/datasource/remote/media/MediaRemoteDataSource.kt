package com.far_sstrwnt.cinemania.shared.data.datasource.remote.media

import com.far_sstrwnt.cinemania.shared.data.datasource.remote.api.ResultsResponse
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.model.*
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.shared.result.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
        private val service: TmdbService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getMediaTrending(mediaType: String): Result<List<NetworkMediaEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaTrending(mediaType).results)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaGenre(mediaType: String): Result<List<NetworkGenreEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaGenre(mediaType).genres)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaDetail(mediaType: String, id: String): Result<NetworkMediaEntity> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaDetail(mediaType, id))
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaCast(mediaType: String, id: String): Result<List<NetworkCastEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaCredits(mediaType, id).cast)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaVideos(mediaType: String, id: String): Result<List<NetworkVideoEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaVideos(mediaType, id).results)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getTvSeason(id: String, seasonNumber: Int): Result<List<NetworkEpisodeEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getTvSeason(id, seasonNumber).episodes)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaSimilar(mediaType: String, id: String, page: Int): ResultsResponse<NetworkMediaEntity> {
        return service.getMediaSimilar(mediaType, id, page)
    }

    suspend fun getMediaByCategory(
        mediaType: String, category: String, page: Int
    ): ResultsResponse<NetworkMediaEntity> {
        return service.getMediaByCategory(mediaType, category, page)
    }

    suspend fun getMediaByAction(
        action: String, mediaType: String, genre: String?, query: String?, page: Int
    ): ResultsResponse<NetworkMediaEntity> {
        return service.getMediaByAction(action, mediaType, genre, query, page)
    }
}