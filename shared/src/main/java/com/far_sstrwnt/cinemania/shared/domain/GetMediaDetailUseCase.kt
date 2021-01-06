package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMediaDetailUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(mediaType: String, id: String): Result<MediaEntity> {
        return repository.getMediaDetail(mediaType, id)
    }
}