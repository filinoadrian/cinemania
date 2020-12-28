package com.far_sstrwnt.cinemania.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.GetMovieDiscoverUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val getMediaGenreUseCase: GetMediaGenreUseCase,
    private val getMovieDiscoverUseCase: GetMovieDiscoverUseCase
) : ViewModel(), EventActionsHandler {

    private val _genreList = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val genreList: LiveData<List<GenreEntity>>
        get() = _genreList

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>>
        get() = _navigateToMovieDetailAction

    override fun openDetail(entity: Entity, id: String) {
        _navigateToMovieDetailAction.value = Event(id)
    }

    private var currentGenreValue: String? = null

    private var currentMovieResult: Flow<PagingData<MovieEntity>>? = null

    fun fetchDiscover(genre: String?): Flow<PagingData<MovieEntity>> {
        val lastResult = currentMovieResult
        if (genre == currentGenreValue && lastResult != null) {
            return lastResult
        }
        currentGenreValue = genre
        val newResult: Flow<PagingData<MovieEntity>> = getMovieDiscoverUseCase(genre)
            .cachedIn(viewModelScope)
        currentMovieResult = newResult
        return newResult
    }

    init {
        fetchGenre(Entity.MOVIE.value)
    }

    private fun fetchGenre(mediaType: String) {
        viewModelScope.launch {
            val genreResult = getMediaGenreUseCase(mediaType)

            if (genreResult is Result.Success) {
                val genre = genreResult.data
                _genreList.value = ArrayList(genre)
            } else {
                _genreList.value = emptyList()
            }
        }
    }
}