package com.far_sstrwnt.cinemania.ui.search

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.ui.common.MovieActions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val fetchMovieSearchUseCase: FetchMovieSearchUseCase
) : ViewModel(), MovieActions {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<MovieEntity>>? = null

    fun searchMovie(queryString: String): Flow<PagingData<MovieEntity>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<MovieEntity>> = fetchMovieSearchUseCase.execute(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>> = _navigateToMovieDetailAction

    override fun openMovieDetail(id: String) {
        _navigateToMovieDetailAction.value = Event(id)
    }
}