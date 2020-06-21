package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.MoviePagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.MovieRemoteDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>>
}

@Singleton
class DefaultMovieRepository @Inject constructor(
        private val dataSource: MovieRemoteDataSource
) : MovieRepository {

    override fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = {
                MoviePagingSource(dataSource, query)
            }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}