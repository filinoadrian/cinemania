package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import javax.inject.Inject

class FetchTvOnTheAirUseCase @Inject constructor(
    private val repository: TvRepository
) {

    fun execute() = repository.getTvOnTheAirResultStream()
}