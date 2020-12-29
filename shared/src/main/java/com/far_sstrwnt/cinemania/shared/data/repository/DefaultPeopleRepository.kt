package com.far_sstrwnt.cinemania.shared.data.repository

import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.people.PeopleRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface PeopleRepository {
    suspend fun getPeopleDetail(id: String): Result<PeopleEntity>
    suspend fun getPeopleMovieCredit(id: String): Result<List<MovieEntity>>
    suspend fun getPeopleTvCredit(id: String): Result<List<TvEntity>>
}

@Singleton
class DefaultPeopleRepository @Inject constructor(
        private val dataSource: PeopleRemoteDataSource
) : PeopleRepository {

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