package com.far_sstrwnt.cinemania.shared.domain.media

import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetMediaGenreUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(mediaType: String): Result<List<GenreEntity>> {
        return repository.getMediaGenre(mediaType)
    }
}