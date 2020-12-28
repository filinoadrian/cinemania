package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.data.datasource.tv.*
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface TvRepository {
    suspend fun getCast(entity: String, id: String): Result<List<CastEntity>>
    suspend fun getVideo(entity: String, id: String): Result<List<VideoEntity>>
    fun getTvResultStream(path: String, genre: String? = null, query: String? = null): Flow<PagingData<TvEntity>>
    fun getTvByCategoryResultStream(category: String): Flow<PagingData<TvEntity>>
    fun getSimilarResultStream(id: String): Flow<PagingData<TvEntity>>
    suspend fun getTvDetail(id: String): Result<TvEntity>
}

@Singleton
class DefaultTvRepository @Inject constructor(
        private val dataSource: TvRemoteDataSource,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TvRepository, BaseRepository(dataSource, ioDispatcher) {

    override fun getTvResultStream(
        path: String, genre: String?, query: String?
    ): Flow<PagingData<TvEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                TvPagingSource(
                    dataSource = dataSource, path = path, genre = genre, query = query
                )
            }
        ).flow
    }

    override fun getTvByCategoryResultStream(category: String): Flow<PagingData<TvEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                TvCategoryPagingSource(dataSource = dataSource, category = category)
            }
        ).flow
    }

    override fun getSimilarResultStream(id: String): Flow<PagingData<TvEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                TvSimilarPagingSource(dataSource, id)
            }
        ).flow
    }

    override suspend fun getTvDetail(id: String): Result<TvEntity> {
        return withContext(Dispatchers.IO) {
            val tv = dataSource.getTvDetail(id)

            (tv as? Result.Success)?.let {
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