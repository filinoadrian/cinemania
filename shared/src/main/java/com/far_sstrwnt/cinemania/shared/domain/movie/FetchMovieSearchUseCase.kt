package com.far_sstrwnt.cinemania.shared.domain.movie

import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import javax.inject.Inject

class FetchMovieSearchUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun execute(query: String)
            = repository.getSearchResultStream(query)
}