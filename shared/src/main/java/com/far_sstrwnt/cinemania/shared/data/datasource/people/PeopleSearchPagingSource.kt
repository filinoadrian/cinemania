package com.far_sstrwnt.cinemania.shared.data.datasource.people

import androidx.paging.PagingSource
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TMDB_STARTING_PAGE_INDEX = 1

class PeopleSearchPagingSource @Inject constructor(
    private val dataSource: PeopleRemoteDataSource,
    private val query: String
) : PagingSource<Int, PeopleEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PeopleEntity> {
        val position = params.key ?: TMDB_STARTING_PAGE_INDEX
        return try {
            val response = dataSource.peopleSearch(query, position)
            val people = response.results.map {
                it.asDomainModel()
            }
            LoadResult.Page(
                data = people,
                prevKey = if (position == TMDB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (people.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}