package com.far_sstrwnt.cinemania.shared.domain.movie

import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import javax.inject.Inject

class FetchMovieNowPlayingUseCase @Inject constructor(
private val repository: MovieRepository
) {

    fun execute() = repository.getNowPlayingResultStream()
}