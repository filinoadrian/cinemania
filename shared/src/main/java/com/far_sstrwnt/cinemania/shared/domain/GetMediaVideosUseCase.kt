package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMediaVideosUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(mediaType: String, id: String): Result<List<VideoEntity>> {
        return repository.getMediaVideos(mediaType, id)
    }
}