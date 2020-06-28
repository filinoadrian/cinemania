package com.far_sstrwnt.cinemania.shared.data.repository

import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.people.PeopleRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface PeopleRepository {
    suspend fun getPeopleDetail(id: String): Result<PeopleEntity>
}

@Singleton
class DefaultPeopleRepository @Inject constructor(
        private val dataSource: PeopleRemoteDataSource
) : PeopleRepository {

    override suspend fun getPeopleDetail(id: String): Result<PeopleEntity> {
        return withContext(Dispatchers.IO) {
            val people = dataSource.peopleDetail(id)

            (people as? Result.Success)?.let {
                return@withContext Result.Success(it.data.asDomainModel())
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }
}