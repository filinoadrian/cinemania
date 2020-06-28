package com.far_sstrwnt.cinemania.shared.domain.movie

import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import javax.inject.Inject

class FetchMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute(id: String)
            = repository.getMovieDetail(id)
}