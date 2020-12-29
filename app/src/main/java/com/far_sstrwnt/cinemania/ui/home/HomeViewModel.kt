package com.far_sstrwnt.cinemania.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaByCategoryUseCase
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaTrendingUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result.Success
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getMediaTrendingUseCase: GetMediaTrendingUseCase,
    private val getMediaGenreUseCase: GetMediaGenreUseCase,
    private val getMediaByCategoryUseCase: GetMediaByCategoryUseCase
) : ViewModel(), MediaActionsHandler {

    private val _mediaTrending = MutableLiveData<List<MediaEntity>>().apply { value = emptyList() }
    val mediaTrending: LiveData<List<MediaEntity>> = _mediaTrending

    private val _mediaGenre = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val mediaGenre: LiveData<List<GenreEntity>> = _mediaGenre

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>> = _navigateToMovieDetailAction

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>> = _navigateToTvDetailAction

    fun fetchMediaTrending(mediaType: String) {
        viewModelScope.launch {
            val trendingResult = getMediaTrendingUseCase(mediaType)

            if (trendingResult is Success) {
                val trendingList = trendingResult.data
                _mediaTrending.value = ArrayList(trendingList)
            } else {
                _mediaTrending.value = emptyList()
            }
        }
    }

    fun fetchMediaGenre(mediaType: String) {
        viewModelScope.launch {
            val genreResult = getMediaGenreUseCase(mediaType)

            if (genreResult is Success) {
                val genreList = genreResult.data
                _mediaGenre.value = ArrayList(genreList)
            } else {
                _mediaGenre.value = emptyList()
            }
        }
    }

    fun fetchMediaByCategory(mediaType: String, category: String): Flow<PagingData<MediaEntity>> {
        return getMediaByCategoryUseCase(mediaType, category).cachedIn(viewModelScope)
    }

    override fun openDetail(mediaType: String, id: String) {
        when (mediaType) {
            MediaType.MOVIE.value -> {
                _navigateToMovieDetailAction.value = Event(id)
            }
            MediaType.TV.value -> {
                _navigateToTvDetailAction.value = Event(id)
            }
        }
    }
}