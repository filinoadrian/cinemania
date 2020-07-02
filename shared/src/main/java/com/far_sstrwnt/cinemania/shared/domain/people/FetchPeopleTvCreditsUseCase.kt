package com.far_sstrwnt.cinemania.shared.domain.people

import com.far_sstrwnt.cinemania.shared.data.repository.PeopleRepository
import javax.inject.Inject

class FetchPeopleTvCreditsUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    suspend fun execute(id: String)
            = repository.getPeopleTvCredit(id)
}