package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import javax.inject.Inject

class FetchTvGenreUseCase @Inject constructor(
    private val repository: TvRepository
) {
    suspend fun execute() = repository.getTvGenreList()
}