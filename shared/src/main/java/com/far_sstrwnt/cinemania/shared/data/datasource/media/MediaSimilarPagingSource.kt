package com.far_sstrwnt.cinemania.shared.data.datasource.media

import androidx.paging.PagingSource
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MediaSimilarPagingSource @Inject constructor(
    private val dataSource: MediaRemoteDataSource,
    private val mediaType: String,
    private val id: String
) : PagingSource<Int, MediaEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaEntity> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = dataSource.getMediaSimilar(mediaType, id, position)
            val mediaList = response.results.map {
                it.asDomainModel()
            }
            LoadResult.Page(
                data = mediaList,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position -1,
                nextKey = if (mediaList.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}