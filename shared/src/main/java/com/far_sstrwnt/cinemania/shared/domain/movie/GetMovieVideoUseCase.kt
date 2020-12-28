package com.far_sstrwnt.cinemania.shared.domain.movie

import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMovieVideoUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    suspend operator fun invoke(id: String): Result<List<VideoEntity>> {
        return repository.getVideo(Entity.MOVIE.value, id)
    }
}