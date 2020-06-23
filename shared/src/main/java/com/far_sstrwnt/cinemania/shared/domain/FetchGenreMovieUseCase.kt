package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import javax.inject.Inject

class FetchGenreMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute() = repository.getGenreMovieList()
}