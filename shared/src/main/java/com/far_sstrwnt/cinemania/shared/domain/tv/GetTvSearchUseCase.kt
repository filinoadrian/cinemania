package com.far_sstrwnt.cinemania.shared.domain.tv

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTvSearchUseCase @Inject constructor(
    private val repository: TvRepository
) {

    operator fun invoke(query: String): Flow<PagingData<TvEntity>> {
        return repository.getTvResultStream(path = "search", query = query)
    }
}