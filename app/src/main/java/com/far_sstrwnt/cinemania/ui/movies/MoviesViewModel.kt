package com.far_sstrwnt.cinemania.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.DiscoverMovieUseCase
import com.far_sstrwnt.cinemania.shared.domain.FetchGenreMovieUseCase
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val discoverMovieUseCase: DiscoverMovieUseCase,
    private val fetchGenreMovieUseCase: FetchGenreMovieUseCase
) : ViewModel() {

    private val _genreList = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val genreList: LiveData<List<GenreEntity>>
        get() = _genreList

    private var currentGenreValue: String? = null

    private var currentMovieResult: Flow<PagingData<MovieEntity>>? = null

    fun discoverMovie(genre: String?): Flow<PagingData<MovieEntity>> {
        val lastResult = currentMovieResult
        if (genre == currentGenreValue && lastResult != null) {
            return lastResult
        }
        currentGenreValue = genre
        val newResult: Flow<PagingData<MovieEntity>> = discoverMovieUseCase.execute(genre)
            .cachedIn(viewModelScope)
        currentMovieResult = newResult
        return newResult
    }

    init {
        loadGenreMovie()
    }

    private fun loadGenreMovie() {
        viewModelScope.launch {
            val genreResult = fetchGenreMovieUseCase.execute()

            if (genreResult is Result.Success) {
                val genre = genreResult.data
                _genreList.value = ArrayList(genre)
            } else {
                _genreList.value = emptyList()
            }
        }
    }
}