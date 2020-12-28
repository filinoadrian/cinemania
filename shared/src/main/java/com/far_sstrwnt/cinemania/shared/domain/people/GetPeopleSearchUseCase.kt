package com.far_sstrwnt.cinemania.shared.domain.people

import androidx.paging.PagingData
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.shared.data.repository.PeopleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPeopleSearchUseCase @Inject constructor(
    private val repository: PeopleRepository
) {

    operator fun invoke(query: String): Flow<PagingData<PeopleEntity>> {
        return repository.getPeopleSearchResultStream(query)
    }
}