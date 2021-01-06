package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import javax.inject.Inject

class InsertMediaFavoriteUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(mediaEntity: MediaEntity) {
        return repository.insertMediaFavorite(mediaEntity)
    }
}