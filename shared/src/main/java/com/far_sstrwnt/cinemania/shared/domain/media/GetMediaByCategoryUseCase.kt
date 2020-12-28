package com.far_sstrwnt.cinemania.shared.domain.media

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaByCategoryUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(mediaType: String, category: String): Flow<PagingData<MediaEntity>> {
        return repository.getMediaByCategoryResultStream(mediaType, category)
    }
}