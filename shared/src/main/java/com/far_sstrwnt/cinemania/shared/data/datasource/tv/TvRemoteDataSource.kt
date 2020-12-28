package com.far_sstrwnt.cinemania.shared.data.datasource.tv

import com.far_sstrwnt.cinemania.shared.data.datasource.BaseRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.api.ResultsResponse
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkTvEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TvRemoteDataSource @Inject constructor(
        private val service: TmdbService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteDataSource(service, ioDispatcher) {
    suspend fun getTv(path: String, genre: String?, query: String?, page: Int): ResultsResponse<NetworkTvEntity> {
        return service.getTv(path, genre, query, page)
    }

    suspend fun getTvByCategory(category: String, page: Int): ResultsResponse<NetworkTvEntity> {
        return service.getTvByCategory(category, page)
    }

    suspend fun getTvSimilar(id: String, page: Int): ResultsResponse<NetworkTvEntity> {
        return service.getTvSimilar(id, page)
    }

    suspend fun getTvDetail(id: String): Result<NetworkTvEntity> {
        return try {
            Result.Success(service.getTvDetail(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}