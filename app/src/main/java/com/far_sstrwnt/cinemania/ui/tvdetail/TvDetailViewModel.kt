package com.far_sstrwnt.cinemania.ui.tvdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvDetailUseCase
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class TvDetailViewModel @Inject constructor(
    private val fetchTvDetailUseCase: FetchTvDetailUseCase
) : ViewModel() {

    private val _tv = MutableLiveData<TvEntity>()
    val tv: LiveData<TvEntity>
        get() = _tv

    fun loadTvDetail(id: String) {
        viewModelScope.launch {
            val tvResult = fetchTvDetailUseCase.execute(id)

            if (tvResult is Result.Success) {
                val tv = tvResult.data
                _tv.value = tv
            }
        }
    }
}