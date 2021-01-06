package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMediaCastUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(mediaType: String, id: String): Result<List<CastEntity>> {
        return repository.getMediaCast(mediaType, id)
    }
}