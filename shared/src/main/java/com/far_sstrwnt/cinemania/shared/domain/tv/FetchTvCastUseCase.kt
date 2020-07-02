package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import javax.inject.Inject

class FetchTvCastUseCase @Inject constructor(
    private val repository: TvRepository
) {
    suspend fun execute(id: String)
            = repository.getTvCastList(id)
}