package com.far_sstrwnt.cinemania.shared.data.repository

import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.BaseRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    private val dataSource: BaseRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getCast(entity: String, id: String): Result<List<CastEntity>> {
        return withContext(Dispatchers.IO) {
            val cast = dataSource.getCast(entity, id)

            (cast as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    suspend fun getVideo(entity: String, id: String): Result<List<VideoEntity>> {
        return withContext(Dispatchers.IO) {
            val video = dataSource.getVideo(entity, id)

            (video as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }
}