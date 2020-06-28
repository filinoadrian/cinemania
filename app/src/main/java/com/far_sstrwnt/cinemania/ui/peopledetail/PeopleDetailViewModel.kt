package com.far_sstrwnt.cinemania.ui.peopledetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.shared.domain.people.FetchPeopleDetailUseCase
import com.far_sstrwnt.cinemania.shared.domain.people.FetchPeopleMovieCreditsUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActions
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeopleDetailViewModel @Inject constructor(
    private val fetchPeopleDetailUseCase: FetchPeopleDetailUseCase,
    private val fetchPeopleMovieCreditsUseCase: FetchPeopleMovieCreditsUseCase
) : ViewModel(), EventActions {

    private val _people = MutableLiveData<PeopleEntity>()
    val people: LiveData<PeopleEntity>
        get() = _people

    private val _movies = MutableLiveData<List<MovieEntity>>()
    val movies: LiveData<List<MovieEntity>>
        get() = _movies

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>>
        get() = _navigateToMovieDetailAction

    override fun openDetail(entity: Entity, id: String) {
        _navigateToMovieDetailAction.value = Event(id)
    }

    fun loadPeopleDetail(id: String) {
        viewModelScope.launch {
            val peopleResult = fetchPeopleDetailUseCase.execute(id)

            if (peopleResult is Result.Success) {
                val people = peopleResult.data
                _people.value = people
            }
        }
    }

    fun loadPeopleMovieCredit(id: String) {
        viewModelScope.launch {
            val movieCreditResult = fetchPeopleMovieCreditsUseCase.execute(id)

            if (movieCreditResult is Result.Success) {
                val movies = movieCreditResult.data
                _movies.value = movies
            }
        }
    }
}