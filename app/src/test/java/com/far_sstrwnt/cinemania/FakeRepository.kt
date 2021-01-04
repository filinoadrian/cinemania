package com.far_sstrwnt.cinemania

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.shared.result.Result.Error
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.flow.Flow

class FakeRepository : MediaRepository {

    var mediaTrendingData: LinkedHashMap<String, MediaEntity> = LinkedHashMap()

    private var shouldReturnError = false

    override suspend fun getMediaFavorite(): Result<List<MediaEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaFavoriteById(id: String): Result<MediaEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertMediaFavorite(mediaEntity: MediaEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMediaFavoriteById(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaTrending(mediaType: String): Result<List<MediaEntity>> {
        if (shouldReturnError) {
            return Error(Exception("Test Exception"))
        }
        return Success(mediaTrendingData.values.toList())
    }

    override suspend fun getMediaGenre(mediaType: String): Result<List<GenreEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaDetail(mediaType: String, id: String): Result<MediaEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaCast(mediaType: String, id: String): Result<List<CastEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMediaVideos(mediaType: String, id: String): Result<List<VideoEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTvSeason(id: String, seasonNumber: Int): Result<List<EpisodeEntity>> {
        TODO("Not yet implemented")
    }

    override fun getMediaSimilarResultStream(
        mediaType: String,
        id: String
    ): Flow<PagingData<MediaEntity>> {
        TODO("Not yet implemented")
    }

    override fun getMediaByCategoryResultStream(
        mediaType: String,
        category: String
    ): Flow<PagingData<MediaEntity>> {
        TODO("Not yet implemented")
    }

    override fun getMediaByActionResultStream(
        action: String,
        mediaType: String,
        genre: String?,
        query: String?
    ): Flow<PagingData<MediaEntity>> {
        TODO("Not yet implemented")
    }

    @VisibleForTesting
    fun addMediaTrending(vararg mediaList: MediaEntity) {
        for (media in mediaList) {
            mediaTrendingData[media.id] = media
        }
    }
}