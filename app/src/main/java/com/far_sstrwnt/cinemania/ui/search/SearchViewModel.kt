package com.far_sstrwnt.cinemania.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.domain.GetMediaSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getMediaSearchUseCase: GetMediaSearchUseCase
) : ViewModel(), MediaActionsHandler {

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String> = _currentQueryValue

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    fun setQueryValue(query: String) {
        _currentQueryValue.value = query
    }

    fun fetchMediaSearch(mediaType: String, query: String): Flow<PagingData<MediaEntity>> {
        return getMediaSearchUseCase(mediaType, query)
            .cachedIn(viewModelScope)
    }

    override fun openDetail(mediaType: String, id: String) {
        val navigationArgs: Pair<String, String> = Pair(mediaType, id)
        _navigateToMediaDetailAction.value = Event(navigationArgs)
    }
}