package com.far_sstrwnt.cinemania.shared.domain.movie

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieDiscoverUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(genre: String?): Flow<PagingData<MovieEntity>> {
        return repository.getMoviesResultStream(path = "discover", genre = genre)
    }
}