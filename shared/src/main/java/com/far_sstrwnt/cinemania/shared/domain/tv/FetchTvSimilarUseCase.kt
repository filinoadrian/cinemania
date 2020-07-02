package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import javax.inject.Inject

class FetchTvSimilarUseCase @Inject constructor(
    private val repository: TvRepository
) {
    fun execute(id: String)
            = repository.getSimilarResultStream(id)
}