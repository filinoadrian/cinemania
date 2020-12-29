package com.far_sstrwnt.cinemania.shared.data.datasource.media

import com.far_sstrwnt.cinemania.shared.data.datasource.api.ResultsResponse
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkGenreEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkMediaEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.shared.result.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Path
import javax.inject.Inject

class MediaRemoteDataSource @Inject constructor(
        private val service: TmdbService,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getMediaTrending(mediaType: String): Result<List<NetworkMediaEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaTrending(mediaType).results)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaGenre(mediaType: String): Result<List<NetworkGenreEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(service.getMediaGenre(mediaType).genres)
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaByCategory(
        mediaType: String, category: String, page: Int
    ): ResultsResponse<NetworkMediaEntity> {
        return service.getMediaByCategory(mediaType, category, page)
    }

    suspend fun getMediaByAction(
        action: String, mediaType: String, genre: String?, query: String?, page: Int
    ): ResultsResponse<NetworkMediaEntity> {
        return service.getMediaByAction(action, mediaType, genre, query, page)
    }
}