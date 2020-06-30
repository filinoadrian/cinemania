package com.far_sstrwnt.cinemania.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.GenreEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvDiscoverUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvGenreUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvViewModel @Inject constructor(
    private val fetchTvGenreUseCase: FetchTvGenreUseCase,
    private val fetchTvDiscoverUseCase: FetchTvDiscoverUseCase
) : ViewModel(), EventActions {

    private val _genreList = MutableLiveData<List<GenreEntity>>().apply { value = emptyList() }
    val genreList: LiveData<List<GenreEntity>>
        get() = _genreList

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>>
        get() = _navigateToTvDetailAction

    override fun openDetail(entity: Entity, id: String) {
        _navigateToTvDetailAction.value = Event(id)
    }

    private var currentGenreValue: String? = null

    private var currentTvResult: Flow<PagingData<TvEntity>>? = null

    fun discoverTv(genre: String?): Flow<PagingData<TvEntity>> {
        val lastResult = currentTvResult
        if (genre == currentGenreValue && lastResult != null) {
            return lastResult
        }
        currentGenreValue = genre
        val newResult: Flow<PagingData<TvEntity>> = fetchTvDiscoverUseCase.execute(genre)
            .cachedIn(viewModelScope)
        currentTvResult = newResult
        return newResult
    }

    init {
        loadGenreTv()
    }

    private fun loadGenreTv() {
        viewModelScope.launch {
            val genreResult = fetchTvGenreUseCase.execute()

            if (genreResult is Result.Success) {
                val genre = genreResult.data
                _genreList.value = ArrayList(genre)
            } else {
                _genreList.value = emptyList()
            }
        }
    }
}