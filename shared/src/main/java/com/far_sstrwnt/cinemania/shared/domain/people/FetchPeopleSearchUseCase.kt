package com.far_sstrwnt.cinemania.shared.domain.people

import com.far_sstrwnt.cinemania.shared.data.repository.PeopleRepository
import javax.inject.Inject

class FetchPeopleSearchUseCase @Inject constructor(
    private val repository: PeopleRepository
) {

    fun execute(query: String)
            = repository.getSearchResultStream(query)
}