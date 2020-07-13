package com.far_sstrwnt.cinemania.ui.newsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieSearchUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewSearchViewModel @Inject constructor(
    private val fetchMovieSearchUseCase: FetchMovieSearchUseCase,
    private val fetchTvSearchUseCase: FetchTvSearchUseCase
) : ViewModel(), EventActionsHandler {

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String>
        get() = _currentQueryValue

    fun search(queryString: String) {
        _currentQueryValue.value = queryString
    }

    fun searchMovie(queryString: String): Flow<PagingData<MovieEntity>> {
        return fetchMovieSearchUseCase.execute(queryString)
            .cachedIn(viewModelScope)
    }

    fun searchTv(queryString: String): Flow<PagingData<TvEntity>> {
        return fetchTvSearchUseCase.execute(queryString)
            .cachedIn(viewModelScope)
    }

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>>
        get() = _navigateToMovieDetailAction

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>>
        get() = _navigateToTvDetailAction

    override fun openDetail(entity: Entity, id: String) {
        if (entity == Entity.MOVIE) {
            _navigateToMovieDetailAction.value = Event(id)
        } else if (entity == Entity.TV) {
            _navigateToTvDetailAction.value = Event(id)
        }
    }
}