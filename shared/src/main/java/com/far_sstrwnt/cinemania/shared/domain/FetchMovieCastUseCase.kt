package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import javax.inject.Inject

class FetchMovieCastUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun execute(id: String)
            = repository.getMovieCastList(id)
}