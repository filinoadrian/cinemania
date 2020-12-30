package com.far_sstrwnt.cinemania.ui.mediadetail

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.EpisodeEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.domain.*
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
    private val getMediaSimilarUseCase: GetMediaSimilarUseCase,
    private val getTvSeasonUseCase: GetTvSeasonUseCase
) : ViewModel(), MediaActionsHandler {

    private val _mediaDetail = MutableLiveData<MediaEntity>()
    val mediaDetail: LiveData<MediaEntity> = _mediaDetail

    private val _mediaCast = MutableLiveData<List<CastEntity>>().apply { value = emptyList() }
    val mediaCast: LiveData<List<CastEntity>> = _mediaCast

    private val _mediaVideos = MutableLiveData<List<VideoEntity>>().apply { value = emptyList() }
    val mediaVideos: LiveData<List<VideoEntity>> = _mediaVideos

    private val _mediaEpisodes = MutableLiveData<List<EpisodeEntity>>().apply { value = emptyList() }
    val mediaEpisodes: LiveData<List<EpisodeEntity>> = _mediaEpisodes

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    val isCastEmpty: LiveData<Boolean> = Transformations.map(_mediaCast) {
        it.isNullOrEmpty()
    }

    val isVideosEmpty: LiveData<Boolean> = Transformations.map(_mediaVideos) {
        it.isNullOrEmpty()
    }

    val isEpisodesEmpty: LiveData<Boolean> = Transformations.map(_mediaEpisodes) {
        it.isNullOrEmpty()
    }

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

    fun fetchMediaEpisodes(id: String, seasonNumber: Int) {
        viewModelScope.launch {
            val episodesResult = getTvSeasonUseCase(id, seasonNumber)

            if (episodesResult is Success) {
                val episodes = episodesResult.data
                _mediaEpisodes.value = episodes
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