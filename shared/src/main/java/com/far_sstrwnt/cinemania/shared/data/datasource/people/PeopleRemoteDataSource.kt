package com.far_sstrwnt.cinemania.shared.data.datasource.people

import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkMovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkPeopleEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class PeopleRemoteDataSource @Inject constructor(
        private val service: TmdbService
) {
    suspend fun peopleDetail(id: String): Result<NetworkPeopleEntity> {
        return try {
            Result.Success(service.getPeopleDetail(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun peopleMovieCredit(id: String): Result<List<NetworkMovieEntity>> {
        return try {
            Result.Success(service.getPeopleMovieCredit(id).cast)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}