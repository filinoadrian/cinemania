package com.far_sstrwnt.cinemania.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.GetMovieSearchUseCase
import com.far_sstrwnt.cinemania.shared.domain.people.GetPeopleSearchUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.GetTvSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getMovieSearchUseCase: GetMovieSearchUseCase,
    private val getTvSearchUseCase: GetTvSearchUseCase,
    private val getPeopleSearchUseCase: GetPeopleSearchUseCase
) : ViewModel(), EventActionsHandler {

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String>
        get() = _currentQueryValue

    fun search(queryString: String) {
        _currentQueryValue.value = queryString
    }

    fun searchMovie(queryString: String): Flow<PagingData<MovieEntity>> {
        return getMovieSearchUseCase(queryString)
            .cachedIn(viewModelScope)
    }

    fun searchTv(queryString: String): Flow<PagingData<TvEntity>> {
        return getTvSearchUseCase(queryString)
            .cachedIn(viewModelScope)
    }

    fun searchPeople(queryString: String): Flow<PagingData<PeopleEntity>> {
        return getPeopleSearchUseCase(queryString)
            .cachedIn(viewModelScope)
    }

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>>
        get() = _navigateToMovieDetailAction

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>>
        get() = _navigateToTvDetailAction

    private val _navigateToPeopleDetailAction = MutableLiveData<Event<String>>()
    val navigateToPeopleDetailAction: LiveData<Event<String>>
        get() = _navigateToPeopleDetailAction

    override fun openDetail(entity: Entity, id: String) {
        when (entity) {
            Entity.MOVIE -> {
                _navigateToMovieDetailAction.value = Event(id)
            }
            Entity.TV -> {
                _navigateToTvDetailAction.value = Event(id)
            }
            Entity.PEOPLE -> {
                _navigateToPeopleDetailAction.value = Event(id)
            }
        }
    }
}