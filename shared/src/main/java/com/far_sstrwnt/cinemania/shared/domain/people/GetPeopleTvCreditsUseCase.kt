package com.far_sstrwnt.cinemania.shared.domain.people

import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.data.repository.PeopleRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetPeopleTvCreditsUseCase @Inject constructor(
    private val repository: PeopleRepository
) {
    suspend operator fun invoke(id: String): Result<List<TvEntity>> {
        return repository.getPeopleTvCredit(id)
    }
}