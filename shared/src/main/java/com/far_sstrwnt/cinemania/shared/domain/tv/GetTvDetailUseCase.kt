package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetTvDetailUseCase @Inject constructor(
    private val repository: TvRepository
) {
    suspend operator fun invoke(id: String): Result<TvEntity> {
        return repository.getTvDetail(id)
    }
}