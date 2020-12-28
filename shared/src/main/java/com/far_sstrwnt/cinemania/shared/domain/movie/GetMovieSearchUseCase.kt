package com.far_sstrwnt.cinemania.shared.domain.movie

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieSearchUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(query: String): Flow<PagingData<MovieEntity>> {
        return repository.getMoviesResultStream(path = "search", query = query)
    }
}