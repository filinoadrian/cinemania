package com.far_sstrwnt.cinemania.ui.mediadetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.domain.GetMediaCastUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaDetailUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaSimilarUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaVideosUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaDetailViewModel @Inject constructor(
    private val getMediaDetailUseCase: GetMediaDetailUseCase,
    private val getMediaCastUseCase: GetMediaCastUseCase,
    private val getMediaVideosUseCase: GetMediaVideosUseCase,
    private val getMediaSimilarUseCase: GetMediaSimilarUseCase
) : ViewModel(), MediaActionsHandler {

    private val _mediaDetail = MutableLiveData<MediaEntity>()
    val mediaDetail: LiveData<MediaEntity> = _mediaDetail

    private val _mediaCast = MutableLiveData<List<CastEntity>>()
    val mediaCast: LiveData<List<CastEntity>> = _mediaCast

    private val _mediaVideos = MutableLiveData<List<VideoEntity>>()
    val mediaVideos: LiveData<List<VideoEntity>> = _mediaVideos

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    fun fetchMediaDetail(mediaType: String, id: String) {
        viewModelScope.launch {
            val mediaResult = getMediaDetailUseCase(mediaType, id)

            if (mediaResult is Success) {
                val media = mediaResult.data
                _mediaDetail.value = media
            }
        }
    }

    fun fetchMediaCast(mediaType: String, id: String) {
        viewModelScope.launch {
            val castResult = getMediaCastUseCase(mediaType, id)

            if (castResult is Success) {
                val cast = castResult.data
                _mediaCast.value = cast
            }
        }
    }

    fun fetchMediaVideos(mediaType: String, id: String) {
        viewModelScope.launch {
            val videosResult = getMediaVideosUseCase(mediaType, id)

            if (videosResult is Success) {
                val videos = videosResult.data
                _mediaVideos.value = videos
            }
        }
    }

    fun fetchMediaSimilar(mediaType: String, id: String): Flow<PagingData<MediaEntity>> {
        return getMediaSimilarUseCase(mediaType, id).cachedIn(viewModelScope)
    }

    override fun openDetail(mediaType: String, id: String) {
        val navigationArgs: Pair<String, String> = Pair(mediaType, id)
        _navigateToMediaDetailAction.value = Event(navigationArgs)
    }
}