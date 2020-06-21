package com.far_sstrwnt.cinemania.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.DiscoverMovieUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val discoverMovieUseCase: DiscoverMovieUseCase
) : ViewModel() {

    fun discoverMovie(): Flow<PagingData<MovieEntity>> {
        return discoverMovieUseCase.execute().cachedIn(viewModelScope)
    }
}