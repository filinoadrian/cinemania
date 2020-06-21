package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.DiscoverMoviePagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.MovieRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.SearchMoviePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>>
    fun getDiscoverResultStream(): Flow<PagingData<MovieEntity>>
}

@Singleton
class DefaultMovieRepository @Inject constructor(
        private val dataSource: MovieRemoteDataSource
) : MovieRepository {

    override fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchMoviePagingSource(dataSource, query)
            }
        ).flow
    }

    override fun getDiscoverResultStream(): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 4,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                DiscoverMoviePagingSource(dataSource)
            }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}