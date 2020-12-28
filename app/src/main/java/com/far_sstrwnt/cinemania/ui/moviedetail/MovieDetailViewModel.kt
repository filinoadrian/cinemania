package com.far_sstrwnt.cinemania.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.GetMovieCastUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.GetMovieDetailUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.GetMovieSimilarUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.GetMovieVideoUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getMovieSimilarUseCase: GetMovieSimilarUseCase,
    private val getMovieVideoUseCase: GetMovieVideoUseCase
) : ViewModel(), EventActionsHandler {

    private val _movie = MutableLiveData<MovieEntity>()
    val movie: LiveData<MovieEntity>
        get() = _movie

    private val _cast = MutableLiveData<List<CastEntity>>()
    val cast: LiveData<List<CastEntity>>
        get() = _cast

    private val _video = MutableLiveData<List<VideoEntity>>()
    val video: LiveData<List<VideoEntity>>
        get() = _video

    private val _navigateToPeopleDetailAction = MutableLiveData<Event<String>>()
    val navigateToPeopleDetailAction: LiveData<Event<String>>
        get() = _navigateToPeopleDetailAction

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>>
        get() = _navigateToMovieDetailAction

    override fun openDetail(entity: Entity, id: String) {
        if (entity == Entity.MOVIE) {
            _navigateToMovieDetailAction.value = Event(id)
        } else if (entity == Entity.PEOPLE) {
            _navigateToPeopleDetailAction.value = Event(id)
        }
    }

    fun fetchDetail(id: String) {
        viewModelScope.launch {
            val movieResult = getMovieDetailUseCase(id)

            if (movieResult is Result.Success) {
                val movie = movieResult.data
                _movie.value = movie
            }
        }
    }

    fun fetchCast(id: String) {
        viewModelScope.launch {
            val castResult = getMovieCastUseCase(id)

            if (castResult is Result.Success) {
                val cast = castResult.data
                _cast.value = cast
            }
        }
    }

    fun fetchVideo(id: String) {
        viewModelScope.launch {
            val videoResult = getMovieVideoUseCase(id)

            if (videoResult is Result.Success) {
                val video = videoResult.data
                _video.value = video
            }
        }
    }

    fun fetchSimilar(id: String): Flow<PagingData<MovieEntity>> {
        return getMovieSimilarUseCase(id).cachedIn(viewModelScope)
    }
}