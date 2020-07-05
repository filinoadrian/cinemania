package com.far_sstrwnt.cinemania.ui.newsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class NewSearchViewModel @Inject constructor() : ViewModel() {

    private val _currentQueryValue = MutableLiveData<String>()
    val currentQueryValue: LiveData<String>
        get() = _currentQueryValue

    fun search(query: String) {
        _currentQueryValue.value = query
    }
}