package com.far_sstrwnt.cinemania.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getMediaSearchUseCase: GetMediaSearchUseCase
) : ViewModel(), MediaActionsHandler {

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String> = _currentQueryValue

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>> = _navigateToMovieDetailAction

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>> = _navigateToTvDetailAction

    private val _navigateToPeopleDetailAction = MutableLiveData<Event<String>>()
    val navigateToPeopleDetailAction: LiveData<Event<String>> = _navigateToPeopleDetailAction

    fun setQueryValue(query: String) {
        _currentQueryValue.value = query
    }

    fun fetchMediaSearch(mediaType: String, query: String): Flow<PagingData<MediaEntity>> {
        return getMediaSearchUseCase(mediaType, query)
            .cachedIn(viewModelScope)
    }

    override fun openDetail(mediaType: String, id: String) {
        when (mediaType) {
            MediaType.MOVIE.value -> {
                _navigateToMovieDetailAction.value = Event(id)
            }
            MediaType.TV.value -> {
                _navigateToTvDetailAction.value = Event(id)
            }
            MediaType.PEOPLE.value -> {
                _navigateToPeopleDetailAction.value = Event(id)
            }
        }
    }
}