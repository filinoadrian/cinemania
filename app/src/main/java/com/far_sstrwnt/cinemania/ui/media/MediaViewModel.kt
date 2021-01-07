package com.far_sstrwnt.cinemania.ui.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.domain.GetMediaDiscoverUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    private val getMediaDiscoverUseCase: GetMediaDiscoverUseCase
) : ViewModel(), MediaActionsHandler {

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    private var currentGenreValue: String? = null

    private var currentMediaResult: Flow<PagingData<MediaEntity>>? = null

    fun fetchMediaDiscover(mediaType: String, genre: String?): Flow<PagingData<MediaEntity>> {
        val lastResult = currentMediaResult
        if (genre == currentGenreValue && lastResult != null) {
            return lastResult
        }
        currentGenreValue = genre
        val newResult: Flow<PagingData<MediaEntity>> = getMediaDiscoverUseCase(mediaType, genre)
            .cachedIn(viewModelScope)
        currentMediaResult = newResult
        return newResult
    }

    override fun openDetail(mediaType: String, id: String) {
        val navigationArgs: Pair<String, String> = Pair(mediaType, id)
        _navigateToMediaDetailAction.value = Event(navigationArgs)
    }
}