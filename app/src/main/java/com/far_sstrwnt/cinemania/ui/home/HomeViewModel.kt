package com.far_sstrwnt.cinemania.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.MediaEntity
import com.far_sstrwnt.cinemania.shared.domain.GetMediaByCategoryUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaTrendingUseCase
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

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    fun fetchMediaTrending(mediaType: String) {
        _dataLoading.value = true

        viewModelScope.launch {
            val trendingResult = getMediaTrendingUseCase(mediaType)

            if (trendingResult is Success) {
                val trendingList = trendingResult.data
                _mediaTrending.value = ArrayList(trendingList)
            } else {
                _mediaTrending.value = emptyList()
            }

            _dataLoading.value = false
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
        val navigationArgs: Pair<String, String> = Pair(mediaType, id)
        _navigateToMediaDetailAction.value = Event(navigationArgs)
    }
}