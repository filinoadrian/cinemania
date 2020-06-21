package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import javax.inject.Inject

class DiscoverMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun execute() = repository.getDiscoverResultStream()
}