package com.far_sstrwnt.cinemania.shared.domain.movie

import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: String): Result<MovieEntity> {
        return repository.getMovieDetail(id)
    }
}