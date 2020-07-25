package com.far_sstrwnt.cinemania.shared.data.datasource.tv

import androidx.paging.PagingSource
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TMDB_STARTING_PAGE_INDEX = 1

class TvTopRatedPagingSource @Inject constructor(
    private val dataSource: TvRemoteDataSource
) : PagingSource<Int, TvEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvEntity> {
        val position = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val response = dataSource.tvTopRated(position)
            val tv = response.results.map {
                it.asDomainModel()
            }
            LoadResult.Page(
                data = tv,
                prevKey = if (position == TMDB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (tv.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}