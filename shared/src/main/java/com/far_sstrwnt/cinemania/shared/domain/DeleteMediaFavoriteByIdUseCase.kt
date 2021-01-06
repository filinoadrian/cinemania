package com.far_sstrwnt.cinemania.shared.domain

import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import javax.inject.Inject

class DeleteMediaFavoriteByIdUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: String) {
        return repository.deleteMediaFavoriteById(id)
    }
}