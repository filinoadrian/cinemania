package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.tv.TvDiscoverPagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.tv.TvRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface TvRepository {
    suspend fun getTvGenreList(): Result<List<GenreEntity>>
    fun getDiscoverResultStream(genre: String?): Flow<PagingData<TvEntity>>
    suspend fun getTvDetail(id: String): Result<TvEntity>
    suspend fun getTvCastList(id: String): Result<List<CastEntity>>
}

@Singleton
class DefaultTvRepository @Inject constructor(
        private val dataSource: TvRemoteDataSource
) : TvRepository {

    override suspend fun getTvGenreList(): Result<List<GenreEntity>> {
        return withContext(Dispatchers.IO) {
            val genres = dataSource.tvGenre()

            (genres as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    override fun getDiscoverResultStream(genre: String?): Flow<PagingData<TvEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                TvDiscoverPagingSource(
                    dataSource, genre
                )
            }
        ).flow
    }

    override suspend fun getTvDetail(id: String): Result<TvEntity> {
        return withContext(Dispatchers.IO) {
            val tv = dataSource.tvDetail(id)

            (tv as? Result.Success)?.let {
                return@withContext Result.Success(it.data.asDomainModel())
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    override suspend fun getTvCastList(id: String): Result<List<CastEntity>> {
        return withContext(Dispatchers.IO) {
            val cast = dataSource.tvCast(id)

            (cast as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
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