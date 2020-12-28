package com.far_sstrwnt.cinemania.shared.data.datasource

import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkMediaEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkCastEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkGenreEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkVideoEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class BaseRemoteDataSource @Inject constructor(
        private val service: TmdbService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getCast(entity: String, id: String): Result<List<NetworkCastEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(service.getCredit(entity, id).cast)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getVideo(entity: String, id: String): Result<List<NetworkVideoEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(service.getVideo(entity, id).results)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}