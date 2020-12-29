package com.far_sstrwnt.cinemania.shared.domain.media

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaDiscoverUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(mediaType: String, genre: String?): Flow<PagingData<MediaEntity>> {
        return repository.getMediaByActionResultStream(action = "discover", mediaType = mediaType, genre = genre)
    }
}