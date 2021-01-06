package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.data.datasource.local.MediaLocalDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.media.MediaByCategoryPagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.media.MediaByActionPagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.media.MediaRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.media.MediaSimilarPagingSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.shared.result.Result.Error
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultMediaRepository @Inject constructor(
    private val localDataSource: MediaLocalDataSource,
    private val remoteDataSource: MediaRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MediaRepository {
    override suspend fun getMediaFavorite(): Result<List<MediaEntity>> {
        return withContext(ioDispatcher) {
            val mediaFavoriteResult = localDataSource.getMediaFavorite()

            (mediaFavoriteResult as? Success)?.let {
                return@withContext Success(it.data)
            }

            return@withContext Error(Exception("Local data source fetch media favorite failed"))
        }
    }

    override suspend fun getMediaFavoriteById(id: String): Result<MediaEntity> {
        return withContext(ioDispatcher) {
            val mediaFavoriteResult = localDataSource.getMediaFavoriteById(id)

            (mediaFavoriteResult as? Success)?.let {
                return@withContext Success(it.data)
            }

            return@withContext Error(Exception("Local data source fetch media favorite by id failed"))
        }
    }

    override suspend fun insertMediaFavorite(mediaEntity: MediaEntity) {
        coroutineScope {
            launch { localDataSource.insertMediaFavorite(mediaEntity) }
        }
    }

    override suspend fun deleteMediaFavoriteById(id: String) {
        coroutineScope {
            launch { localDataSource.deleteMediaFavoriteById(id) }
        }
    }

    override suspend fun getMediaTrending(mediaType: String): Result<List<MediaEntity>> {
        return withContext(ioDispatcher) {
            val mediaResult = remoteDataSource.getMediaTrending(mediaType)

            (mediaResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media trending failed"))
        }
    }

    override suspend fun getMediaGenre(mediaType: String): Result<List<GenreEntity>> {
        return withContext(ioDispatcher) {
            val genreResult = remoteDataSource.getMediaGenre(mediaType)

            (genreResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media genre failed"))
        }
    }

    override suspend fun getMediaDetail(mediaType: String, id: String): Result<MediaEntity> {
        return withContext(ioDispatcher) {
            val mediaResult = remoteDataSource.getMediaDetail(mediaType, id)

            (mediaResult as? Success)?.let {
                return@withContext Success(it.data.asDomainModel())
            }

            return@withContext Error(Exception("Remote data source fetch media detail failed"))
        }
    }

    override suspend fun getMediaCast(mediaType: String, id: String): Result<List<CastEntity>> {
        return withContext(Dispatchers.IO) {
            val castResult = remoteDataSource.getMediaCast(mediaType, id)

            (castResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media cast failed"))
        }
    }

    override suspend fun getMediaVideos(mediaType: String, id: String): Result<List<VideoEntity>> {
        return withContext(ioDispatcher) {
            val videoResult = remoteDataSource.getMediaVideos(mediaType, id)

            (videoResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch media videos failed"))
        }
    }

    override suspend fun getTvSeason(id: String, seasonNumber: Int): Result<List<EpisodeEntity>> {
        return withContext(ioDispatcher) {
            val episodeResult = remoteDataSource.getTvSeason(id, seasonNumber)

            (episodeResult as? Success)?.let {
                return@withContext Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Error(Exception("Remote data source fetch tv series season failed"))
        }
    }

    override fun getMediaSimilarResultStream(mediaType: String, id: String): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MediaSimilarPagingSource(remoteDataSource, mediaType, id)
            }
        ).flow
    }

    override fun getMediaByCategoryResultStream(mediaType: String, category: String): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MediaByCategoryPagingSource(remoteDataSource, mediaType, category)
            }
        ).flow
    }

    override fun getMediaByActionResultStream(
        action: String, mediaType: String, genre: String?, query: String?
    ): Flow<PagingData<MediaEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MediaByActionPagingSource(remoteDataSource, action, mediaType, genre, query)
            }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
        private const val NETWORK_PREFETCH_DISTANCE = 4
        private const val NETWORK_ENABLE_PLACEHOLDERS = false
    }
}