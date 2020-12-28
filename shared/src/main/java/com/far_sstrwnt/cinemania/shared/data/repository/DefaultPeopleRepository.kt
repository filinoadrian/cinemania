package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.people.PeopleRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.people.PeopleSearchPagingSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface PeopleRepository {
    fun getPeopleSearchResultStream(query: String): Flow<PagingData<PeopleEntity>>
    suspend fun getPeopleDetail(id: String): Result<PeopleEntity>
    suspend fun getPeopleMovieCredit(id: String): Result<List<MovieEntity>>
    suspend fun getPeopleTvCredit(id: String): Result<List<TvEntity>>
}

@Singleton
class DefaultPeopleRepository @Inject constructor(
        private val dataSource: PeopleRemoteDataSource
) : PeopleRepository {

    override fun getPeopleSearchResultStream(query: String): Flow<PagingData<PeopleEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                PeopleSearchPagingSource(
                    dataSource, query
                )
            }
        ).flow
    }

    override suspend fun getPeopleDetail(id: String): Result<PeopleEntity> {
        return withContext(Dispatchers.IO) {
            val people = dataSource.getPeopleDetail(id)

            (people as? Result.Success)?.let {
                return@withContext Result.Success(it.data.asDomainModel())
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    override suspend fun getPeopleMovieCredit(id: String): Result<List<MovieEntity>> {
        return withContext(Dispatchers.IO) {
            val movies = dataSource.getPeopleMovieCredit(id)

            (movies as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    override suspend fun getPeopleTvCredit(id: String): Result<List<TvEntity>> {
        return withContext(Dispatchers.IO) {
            val tv = dataSource.getPeopleTvCredit(id)

            (tv as? Result.Success)?.let {
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