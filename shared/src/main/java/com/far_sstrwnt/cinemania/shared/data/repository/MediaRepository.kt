package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.data.datasource.media.MediaByCategoryPagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.media.MediaByActionPagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.media.MediaRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.media.MediaSimilarPagingSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.shared.result.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val dataSource: MediaRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getMediaTrending(mediaType: String): Result<List<MediaEntity>> {
        return withContext(ioDispatcher) {
            val mediaResult = dataSource.getMediaTrending(mediaType)

            (mediaResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media trending failed"))
        }
    }

    suspend fun getMediaGenre(mediaType: String): Result<List<GenreEntity>> {
        return withContext(ioDispatcher) {
            val genreResult = dataSource.getMediaGenre(mediaType)

            (genreResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media genre failed"))
        }
    }

    suspend fun getMediaDetail(mediaType: String, id: String): Result<MediaEntity> {
        return withContext(ioDispatcher) {
            val mediaResult = dataSource.getMediaDetail(mediaType, id)

            (mediaResult as? Success)?.let {
                return@withContext Success(it.data.asDomainModel())
            }

            return@withContext Error(Exception("Remote data source fetch media detail failed"))
        }
    }

    suspend fun getMediaCast(mediaType: String, id: String): Result<List<CastEntity>> {
        return withContext(Dispatchers.IO) {
            val castResult = dataSource.getMediaCast(mediaType, id)

            (castResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media cast failed"))
        }
    }

    suspend fun getMediaVideos(mediaType: String, id: String): Result<List<VideoEntity>> {
        return withContext(ioDispatcher) {
            val videoResult = dataSource.getMediaVideos(mediaType, id)

            (videoResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media videos failed"))
        }
    }

    suspend fun getTvSeason(id: String, seasonNumber: Int): Result<List<EpisodeEntity>> {
        return withContext(ioDispatcher) {
            val episodeResult = dataSource.getTvSeason(id, seasonNumber)

            (episodeResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch tv series season failed"))
        }
    }

    fun getMediaSimilarResultStream(mediaType: String, id: String): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MediaSimilarPagingSource(dataSource, mediaType, id)
            }
        ).flow
    }

    fun getMediaByCategoryResultStream(mediaType: String, category: String): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MediaByCategoryPagingSource(dataSource, mediaType, category)
            }
        ).flow
    }

    fun getMediaByActionResultStream(
        action: String,
        mediaType: String,
        genre: String? = null,
        query: String? = null
    ): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MediaByActionPagingSource(dataSource, action, mediaType, genre, query)
            }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
        private const val NETWORK_PREFETCH_DISTANCE = 4
        private const val NETWORK_ENABLE_PLACEHOLDERS = false
    }
}