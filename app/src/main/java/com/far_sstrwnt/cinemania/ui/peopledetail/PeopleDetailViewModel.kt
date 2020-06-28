package com.far_sstrwnt.cinemania.ui.peopledetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.shared.domain.people.FetchPeopleDetailUseCase
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class PeopleDetailViewModel @Inject constructor(
    private val fetchPeopleDetailUseCase: FetchPeopleDetailUseCase
) : ViewModel() {

    private val _people = MutableLiveData<PeopleEntity>()
    val people: LiveData<PeopleEntity>
        get() = _people

    fun loadPeopleDetail(id: String) {
        viewModelScope.launch {
            val peopleResult = fetchPeopleDetailUseCase.execute(id)

            if (peopleResult is Result.Success) {
                val people = peopleResult.data
                _people.value = people
            }
        }
    }
}