package com.far_sstrwnt.cinemania.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieCastUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieDetailUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.MovieActions
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val fetchMovieDetailUseCase: FetchMovieDetailUseCase,
    private val fetchMovieCastUseCase: FetchMovieCastUseCase
) : ViewModel(), MovieDetailEventListener {

    private val _movie = MutableLiveData<MovieEntity>()
    val movie: LiveData<MovieEntity>
        get() = _movie

    private val _cast = MutableLiveData<List<CastEntity>>()
    val cast: LiveData<List<CastEntity>>
        get() = _cast

    private val _navigateToPeopleDetailAction = MutableLiveData<Event<String>>()
    val navigateToPeopleDetailAction: LiveData<Event<String>>
        get() = _navigateToPeopleDetailAction

    override fun openPeopleDetail(id: String) {
        _navigateToPeopleDetailAction.value = Event(id)
    }

    fun loadMovieDetail(id: String) {
        viewModelScope.launch {
            val movieResult = fetchMovieDetailUseCase.execute(id)

            if (movieResult is Result.Success) {
                val movie = movieResult.data
                _movie.value = movie
            }
        }
    }

    fun loadMovieCast(id: String) {
        viewModelScope.launch {
            val castResult = fetchMovieCastUseCase.execute(id)

            if (castResult is Result.Success) {
                val cast = castResult.data
                _cast.value = cast
            }
        }
    }
}

interface MovieDetailEventListener {
    fun openPeopleDetail(id: String)
}