package com.far_sstrwnt.cinemania.ui.tvdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.model.VideoEntity
import com.far_sstrwnt.cinemania.shared.domain.tv.GetTvCastUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.GetTvDetailUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.GetTvSimilarUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.GetTvVideoUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActionsHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvDetailViewModel @Inject constructor(
    private val getTvDetailUseCase: GetTvDetailUseCase,
    private val getTvCastUseCase: GetTvCastUseCase,
    private val getTvSimilarUseCase: GetTvSimilarUseCase,
    private val getTvVideoUseCase: GetTvVideoUseCase
) : ViewModel(), EventActionsHandler {

    private val _tv = MutableLiveData<TvEntity>()
    val tv: LiveData<TvEntity>
        get() = _tv

    private val _cast = MutableLiveData<List<CastEntity>>()
    val cast: LiveData<List<CastEntity>>
        get() = _cast

    private val _video = MutableLiveData<List<VideoEntity>>()
    val video: LiveData<List<VideoEntity>>
        get() = _video

    private val _navigateToPeopleDetailAction = MutableLiveData<Event<String>>()
    val navigateToPeopleDetailAction: LiveData<Event<String>>
        get() = _navigateToPeopleDetailAction

    private val _navigateToTvDetailAction = MutableLiveData<Event<String>>()
    val navigateToTvDetailAction: LiveData<Event<String>>
        get() = _navigateToTvDetailAction

    override fun openDetail(entity: Entity, id: String) {
        if (entity == Entity.TV) {
            _navigateToTvDetailAction.value = Event(id)
        } else if (entity == Entity.PEOPLE) {
            _navigateToPeopleDetailAction.value = Event(id)
        }
    }

    fun fetchDetail(id: String) {
        viewModelScope.launch {
            val tvResult = getTvDetailUseCase(id)

            if (tvResult is Result.Success) {
                val tv = tvResult.data
                _tv.value = tv
            }
        }
    }

    fun fetchCast(id: String) {
        viewModelScope.launch {
            val castResult = getTvCastUseCase(id)

            if (castResult is Result.Success) {
                val cast = castResult.data
                _cast.value = cast
            }
        }
    }

    fun fetchVideo(id: String) {
        viewModelScope.launch {
            val videoResult = getTvVideoUseCase(id)

            if (videoResult is Result.Success) {
                val video = videoResult.data
                _video.value = video
            }
        }
    }

    fun fetchSimilar(id: String): Flow<PagingData<TvEntity>> {
        return getTvSimilarUseCase(id).cachedIn(viewModelScope)
    }
}