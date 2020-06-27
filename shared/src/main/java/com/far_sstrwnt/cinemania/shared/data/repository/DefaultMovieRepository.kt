package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.DiscoverMoviePagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.MovieRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.SearchMoviePagingSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    suspend fun getGenreMovieList(): Result<List<GenreEntity>>
    fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>>
    fun getDiscoverResultStream(genre: String?): Flow<PagingData<MovieEntity>>
    suspend fun getDetailMovie(id: String): Result<MovieEntity>
}

@Singleton
class DefaultMovieRepository @Inject constructor(
        private val dataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun getGenreMovieList(): Result<List<GenreEntity>> {
        return withContext(Dispatchers.IO) {
            val genres = dataSource.genreMovie()

            (genres as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    override fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                SearchMoviePagingSource(dataSource, query)
            }
        ).flow
    }

    override fun getDiscoverResultStream(genre: String?): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = NETWORK_PREFETCH_DISTANCE,
                enablePlaceholders = NETWORK_ENABLE_PLACEHOLDERS
            ),
            pagingSourceFactory = {
                DiscoverMoviePagingSource(dataSource, genre)
            }
        ).flow
    }

    override suspend fun getDetailMovie(id: String): Result<MovieEntity> {
        return withContext(Dispatchers.IO) {
            val movie = dataSource.detailMovie(id)

            (movie as? Result.Success)?.let {
                return@withContext Result.Success(it.data.asDomainModel())
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
        private const val NETWORK_PREFETCH_DISTANCE = 4
        private const val NETWORK_ENABLE_PLACEHOLDERS = false
    }
}