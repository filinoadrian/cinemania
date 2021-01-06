package com.far_sstrwnt.cinemania.shared.data.datasource.local

import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.shared.result.Result.Error
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaLocalDataSource @Inject constructor(
    private val dao: MediaDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getMediaFavorite(): Result<List<MediaEntity>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(dao.getMediaFavorite())
        } catch (e: Exception) {
            Error(e)
        }
    }

    suspend fun getMediaFavoriteById(id: String): Result<MediaEntity> = withContext(ioDispatcher) {
        try {
            val mediaFavorite = dao.getMediaFavoriteById(id)
            if (mediaFavorite != null) {
                return@withContext Success(mediaFavorite)
            } else {
                return@withContext Error(Exception("Media Favorite not found"))
            }
        } catch (e: Exception) {
            return@withContext Error(e)
        }
    }

    suspend fun insertMediaFavorite(mediaEntity: MediaEntity) = withContext(ioDispatcher) {
        dao.insertMediaFavorite(mediaEntity)
    }

    suspend fun deleteMediaFavoriteById(id: String) = withContext(ioDispatcher) {
        dao.deleteMediaFavoriteById(id)
    }
}