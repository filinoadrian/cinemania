package com.far_sstrwnt.cinemania.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.domain.DeleteMediaFavoriteByIdUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaFavoriteUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(
    private val getMediaFavoriteUseCase: GetMediaFavoriteUseCase,
    private val deleteMediaFavoriteUseCase: DeleteMediaFavoriteByIdUseCase
) : ViewModel(), MediaActionsHandler {

    private val _mediaFavorite = MutableLiveData<List<MediaEntity>>().apply { value = emptyList() }
    val mediaFavorite: LiveData<List<MediaEntity>> = _mediaFavorite

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    fun fetchMediaFavorite() {
        viewModelScope.launch {
            val mediaFavoriteResult = getMediaFavoriteUseCase()

            if (mediaFavoriteResult is Success) {
                val mediaFavorite = mediaFavoriteResult.data
                _mediaFavorite.value = mediaFavorite
            } else {
                _mediaFavorite.value = emptyList()
            }
        }
    }

    fun deleteMediaFavorite(id: String) {
        viewModelScope.launch {
            deleteMediaFavoriteUseCase(id)
        }
    }

    override fun openDetail(mediaType: String, id: String) {
        val navigationArgs: Pair<String, String> = Pair(mediaType, id)
        _navigateToMediaDetailAction.value = Event(navigationArgs)
    }
}