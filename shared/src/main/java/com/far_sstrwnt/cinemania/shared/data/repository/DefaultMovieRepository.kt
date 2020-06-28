package com.far_sstrwnt.cinemania.shared.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.movie.DiscoverMoviePagingSource
import com.far_sstrwnt.cinemania.shared.data.datasource.movie.MovieRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.movie.SearchMoviePagingSource
import com.far_sstrwnt.cinemania.shared.data.mapper.asDomainModel
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface MovieRepository {
    suspend fun getMovieGenreList(): Result<List<GenreEntity>>
    fun getSearchResultStream(query: String): Flow<PagingData<MovieEntity>>
    fun getDiscoverResultStream(genre: String?): Flow<PagingData<MovieEntity>>
    suspend fun getMovieDetail(id: String): Result<MovieEntity>
    suspend fun getMovieCastList(id: String): Result<List<CastEntity>>
}

@Singleton
class DefaultMovieRepository @Inject constructor(
        private val dataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun getMovieGenreList(): Result<List<GenreEntity>> {
        return withContext(Dispatchers.IO) {
            val genres = dataSource.movieGenre()

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
                SearchMoviePagingSource(
                    dataSource,
                    query
                )
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
                DiscoverMoviePagingSource(
                    dataSource,
                    genre
                )
            }
        ).flow
    }

    override suspend fun getMovieDetail(id: String): Result<MovieEntity> {
        return withContext(Dispatchers.IO) {
            val movie = dataSource.movieDetail(id)

            (movie as? Result.Success)?.let {
                return@withContext Result.Success(it.data.asDomainModel())
            }

            return@withContext Result.Error(Exception("Remote data source fetch failed"))
        }
    }

    override suspend fun getMovieCastList(id: String): Result<List<CastEntity>> {
        return withContext(Dispatchers.IO) {
            val cast = dataSource.movieCast(id)

            (cast as? Result.Success)?.let {
                return@withContext Result.Success(it.data.map { results ->
                    results.asDomainModel()
                })
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