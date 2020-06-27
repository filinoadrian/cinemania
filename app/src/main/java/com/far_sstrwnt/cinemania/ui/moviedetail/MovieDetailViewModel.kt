package com.far_sstrwnt.cinemania.ui.moviedetail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.shared.domain.FetchDetailMovieUseCase
import com.far_sstrwnt.cinemania.shared.result.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val fetchDetailMovieUseCase: FetchDetailMovieUseCase
) : ViewModel() {

    private val _movie = MutableLiveData<MovieEntity>()
    val movie: LiveData<MovieEntity>
        get() = _movie

    fun loadMovieDetail(id: String) {
        viewModelScope.launch {
            val movieResult = fetchDetailMovieUseCase.execute(id)

            if (movieResult is Result.Success) {
                val movie = movieResult.data
                _movie.value = movie
            }
        }
    }
}