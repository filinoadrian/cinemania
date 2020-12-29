package com.far_sstrwnt.cinemania.shared.domain

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaSearchUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(mediaType: String, query: String?): Flow<PagingData<MediaEntity>> {
        return repository.getMediaByActionResultStream(action = "search", mediaType = mediaType, query = query)
    }
}