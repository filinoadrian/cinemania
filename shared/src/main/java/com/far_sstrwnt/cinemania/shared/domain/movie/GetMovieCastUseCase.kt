package com.far_sstrwnt.cinemania.shared.domain.movie

import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMovieCastUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: String): Result<List<CastEntity>> {
        return repository.getCast(Entity.MOVIE.value, id)
    }
}