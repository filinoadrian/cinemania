package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.data.datasource.movie.*
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    suspend fun getCast(entity: String, id: String): Result<List<CastEntity>>
    suspend fun getVideo(entity: String, id: String): Result<List<VideoEntity>>
    fun getSimilarResultStream(id: String): Flow<PagingData<MovieEntity>>
    suspend fun getMovieDetail(id: String): Result<MovieEntity>
}

@Singleton
class DefaultMovieRepository @Inject constructor(
        private val dataSource: MovieRemoteDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository, BaseRepository(dataSource, ioDispatcher) {

    override fun getSimilarResultStream(id: String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                MovieSimilarPagingSource(
                    dataSource,
                    id
                )
            }
        ).flow
    }

    override suspend fun getMovieDetail(id: String): Result<MovieEntity> {
        return withContext(Dispatchers.IO) {
            val movie = dataSource.getMovieDetail(id)

            (movie as? Result.Success)?.let {
                return@withContext Result.Success(it.data.asDomainModel())
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
        private const val NETWORK_PREFETCH_DISTANCE = 4
        private const val NETWORK_ENABLE_PLACEHOLDERS = false
    }
}