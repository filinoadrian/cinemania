package com.far_sstrwnt.cinemania.ui.peopledetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.people.GetPeopleDetailUseCase
import com.far_sstrwnt.cinemania.shared.domain.people.GetPeopleMovieCreditsUseCase
import com.far_sstrwnt.cinemania.shared.domain.people.GetPeopleTvCreditsUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeopleDetailViewModel @Inject constructor(
    private val getPeopleDetailUseCase: GetPeopleDetailUseCase,
    private val getPeopleMovieCreditsUseCase: GetPeopleMovieCreditsUseCase,
    private val getPeopleTvCreditsUseCase: GetPeopleTvCreditsUseCase
) : ViewModel(), EventActionsHandler {

    private val _people = MutableLiveData<PeopleEntity>()
    val people: LiveData<PeopleEntity>
        get() = _people

    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>>
        get() = _movies

    private val _tv = MutableLiveData<List<TvEntity>>()
    val tv: LiveData<List<TvEntity>>
        get() = _tv

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

    fun fetchDetail(id: String) {
        viewModelScope.launch {
            val peopleResult = getPeopleDetailUseCase(id)

            if (peopleResult is Result.Success) {
                val people = peopleResult.data
                _people.value = people
            }
        }
    }

    fun fetchMovieCredit(id: String) {
        viewModelScope.launch {
            val movieCreditResult = getPeopleMovieCreditsUseCase(id)

            if (movieCreditResult is Result.Success) {
                val movies = movieCreditResult.data
                _movies.value = movies
            }
        }
    }

    fun fetchTvCredit(id: String) {
        viewModelScope.launch {
            val tvCreditResult = getPeopleTvCreditsUseCase(id)

            if (tvCreditResult is Result.Success) {
                val tv = tvCreditResult.data
                _tv.value = tv
            }
        }
    }
}