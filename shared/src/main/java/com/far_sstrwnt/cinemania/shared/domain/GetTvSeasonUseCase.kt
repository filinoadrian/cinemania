package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.model.EpisodeEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import com.far_sstrwnt.cinemania.shared.result.Result
import javax.inject.Inject

class GetTvSeasonUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: String, seasonNumber: Int): Result<List<EpisodeEntity>> {
        return repository.getTvSeason(id, seasonNumber)
    }
}