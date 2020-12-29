package com.far_sstrwnt.cinemania.shared.data.datasource.movie

import com.far_sstrwnt.cinemania.shared.data.datasource.BaseRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.api.*
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkMovieEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
        private val service: TmdbService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseRemoteDataSource(service, ioDispatcher) {
    suspend fun getMovieSimilar(id: String, page: Int): ResultsResponse<NetworkMovieEntity> {
        return service.getMovieSimilar(id, page)
    }

    suspend fun getMovieDetail(id: String): Result<NetworkMovieEntity> {
        return try {
            Result.Success(service.getMovieDetail(id))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}