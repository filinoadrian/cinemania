package com.far_sstrwnt.cinemania.shared.data.datasource

import androidx.paging.PagingSource
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TMDB_STARTING_PAGE_INDEX = 1

class MoviePagingSource @Inject constructor(
    private val dataSource: MovieRemoteDataSource,
    private val query: String
) : PagingSource<Int, MovieEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieEntity> {
        val position = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val response = dataSource.getSearchMovie(query, position)
            val movies = response.results.map {
                it.asDomainModel()
            }
            LoadResult.Page(
                data = movies,
                prevKey = if (position == TMDB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (movies.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}