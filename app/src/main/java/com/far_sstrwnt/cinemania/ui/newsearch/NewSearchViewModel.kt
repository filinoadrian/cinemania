package com.far_sstrwnt.cinemania.ui.newsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieNewSearchUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class NewSearchViewModel @Inject constructor(
    private val fetchMovieSearchUseCase: FetchMovieSearchUseCase
) : ViewModel(), EventActionsHandler {

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String>
        get() = _currentQueryValue

    var currentSearchResult: Flow<PagingData<MovieEntity>>? = null

    fun search(queryString: String) {
        _currentQueryValue.value = queryString
    }

    fun searchMovie(queryString: String): Flow<PagingData<MovieEntity>> {
        val newResult: Flow<PagingData<MovieEntity>> = fetchMovieSearchUseCase.execute(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>>
        get() = _navigateToMovieDetailAction

    override fun openDetail(entity: Entity, id: String) {
        _navigateToMovieDetailAction.value = Event(id)
    }
}