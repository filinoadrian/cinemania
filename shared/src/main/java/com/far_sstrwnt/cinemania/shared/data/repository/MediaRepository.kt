package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun getMediaFavorite(): Result<List<MediaEntity>>
    suspend fun getMediaFavoriteById(id: String): Result<MediaEntity>
    suspend fun insertMediaFavorite(mediaEntity: MediaEntity)
    suspend fun deleteMediaFavoriteById(id: String)
    suspend fun getMediaTrending(mediaType: String): Result<List<MediaEntity>>
    suspend fun getMediaGenre(mediaType: String): Result<List<GenreEntity>>
    suspend fun getMediaDetail(mediaType: String, id: String): Result<MediaEntity>
    suspend fun getMediaCast(mediaType: String, id: String): Result<List<CastEntity>>
    suspend fun getMediaVideos(mediaType: String, id: String): Result<List<VideoEntity>>
    suspend fun getTvSeason(id: String, seasonNumber: Int): Result<List<EpisodeEntity>>
    fun getMediaSimilarResultStream(mediaType: String, id: String): Flow<PagingData<MediaEntity>>
    fun getMediaByCategoryResultStream(mediaType: String, category: String): Flow<PagingData<MediaEntity>>
    fun getMediaByActionResultStream(
        action: String,
        mediaType: String,
        genre: String? = null,
        query: String? = null
    ): Flow<PagingData<MediaEntity>>
}