package com.far_sstrwnt.cinemania.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.far_sstrwnt.cinemania.data.MovieRepository

class ViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}