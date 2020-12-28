package com.far_sstrwnt.cinemania.shared.domain.tv

import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetTvVideoUseCase @Inject constructor(
    private val repository: TvRepository
) {

    suspend operator fun invoke(id: String): Result<List<VideoEntity>> {
        return repository.getVideo(Entity.TV.value, id)
    }
}