package com.far_sstrwnt.cinemania.ui.tvdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.CastEntity
import com.far_sstrwnt.cinemania.model.Entity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvCastUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvDetailUseCase
import com.far_sstrwnt.cinemania.shared.result.Event
import com.far_sstrwnt.cinemania.shared.result.Result
import com.far_sstrwnt.cinemania.ui.common.EventActions
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvDetailViewModel @Inject constructor(
    private val fetchTvDetailUseCase: FetchTvDetailUseCase,
    private val fetchTvCastUseCase: FetchTvCastUseCase
) : ViewModel(), EventActions {

    private val _tv = MutableLiveData<TvEntity>()
    val tv: LiveData<TvEntity>
        get() = _tv

    private val _cast = MutableLiveData<List<CastEntity>>()
    val cast: LiveData<List<CastEntity>>
        get() = _cast

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

    fun loadTvDetail(id: String) {
        viewModelScope.launch {
            val tvResult = fetchTvDetailUseCase.execute(id)

            if (tvResult is Result.Success) {
                val tv = tvResult.data
                _tv.value = tv
            }
        }
    }

    fun loadTvCast(id: String) {
        viewModelScope.launch {
            val castResult = fetchTvCastUseCase.execute(id)

            if (castResult is Result.Success) {
                val cast = castResult.data
                _cast.value = cast
            }
        }
    }
}