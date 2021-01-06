package com.far_sstrwnt.cinemania.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.R
import com.far_sstrwnt.cinemania.model.*
import com.far_sstrwnt.cinemania.shared.domain.GetMediaGenreUseCase
import com.far_sstrwnt.cinemania.shared.domain.GetMediaSearchUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.MediaActionsHandler
import com.far_sstrwnt.cinemania.util.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getMediaSearchUseCase: GetMediaSearchUseCase,
    private val getMediaGenreUseCase: GetMediaGenreUseCase
) : ViewModel(), MediaActionsHandler {

    private val _movieGenre = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val movieGenre: LiveData<List<GenreEntity>> = _movieGenre

    private val _tvGenre = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val tvGenre: LiveData<List<GenreEntity>> = _tvGenre

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String> = _currentQueryValue

    private val _genreVisible = MutableLiveData<Boolean>().apply { value = true }
    val genreVisible: LiveData<Boolean> = _genreVisible

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarMessage = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarMessage

    private val _navigateToMediaDetailAction = MutableLiveData<Event<Pair<String, String>>>()
    val navigateToMediaDetailAction: LiveData<Event<Pair<String, String>>> = _navigateToMediaDetailAction

    fun setQueryValue(query: String) {
        _currentQueryValue.value = query
    }

    fun setGenreVisibility(value: Boolean) {
        _genreVisible.value = value
    }

    fun fetchMediaSearch(mediaType: String, query: String): Flow<PagingData<MediaEntity>> {
        return getMediaSearchUseCase(mediaType, query)
            .cachedIn(viewModelScope)
    }

    fun fetchMediaGenre(mediaType: String) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val genreResult = getMediaGenreUseCase(mediaType)

                if (genreResult is Result.Success) {
                    val genreList = genreResult.data

                    if (mediaType == MediaType.MOVIE.value) {
                        _movieGenre.value = ArrayList(genreList)
                    } else {
                        _tvGenre.value = ArrayList(genreList)
                    }
                } else {
                    if (mediaType == MediaType.MOVIE.value) {
                        _movieGenre.value = emptyList()
                    } else {
                        _tvGenre.value = emptyList()
                    }

                    showSnackbarMessage(R.string.loading_genre_error)
                }

                _dataLoading.value = false
            }
        }
    }

    override fun openDetail(mediaType: String, id: String) {
        val navigationArgs: Pair<String, String> = Pair(mediaType, id)
        _navigateToMediaDetailAction.value = Event(navigationArgs)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarMessage.value = Event(message)
    }
}