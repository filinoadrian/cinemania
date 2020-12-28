package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetTvCastUseCase @Inject constructor(
    private val repository: TvRepository
) {
    suspend operator fun invoke(id: String): Result<List<CastEntity>> {
        return repository.getCast(Entity.TV.value, id)
    }
}