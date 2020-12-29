package com.far_sstrwnt.cinemania.ui.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.model.MediaType
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaDiscoverUseCase
import com.far_sstrwnt.cinemania.shared.domain.media.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaViewModel @Inject constructor(
    private val getMediaGenreUseCase: GetMediaGenreUseCase,
    private val getMediaDiscoverUseCase: GetMediaDiscoverUseCase
) : ViewModel(), MediaActionsHandler {

    private val _mediaGenre = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val mediaGenre: LiveData<List<GenreEntity>> = _mediaGenre

    private val _navigateToMovieDetailAction = MutableLiveData<Event<String>>()
    val navigateToMovieDetailAction: LiveData<Event<String>> = _navigateToMovieDetailAction

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>> = _navigateToTvDetailAction

    private var currentGenreValue: String? = null

    private var currentMediaResult: Flow<PagingData<MediaEntity>>? = null

    fun fetchMediaGenre(mediaType: String) {
        viewModelScope.launch {
            val genreResult = getMediaGenreUseCase(mediaType)

            if (genreResult is Result.Success) {
                val genreList = genreResult.data
                _mediaGenre.value = ArrayList(genreList)
            } else {
                _mediaGenre.value = emptyList()
            }
        }
    }

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