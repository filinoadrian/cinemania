package com.far_sstrwnt.cinemania

import androidx.lifecycle.ViewModelProvider
import com.far_sstrwnt.cinemania.api.TmdbService
import com.far_sstrwnt.cinemania.data.MovieRepository
import com.far_sstrwnt.cinemania.ui.ViewModelFactory

object Injection {

    private fun provideMovieRepository(): MovieRepository {
        return MovieRepository((TmdbService.create()))
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideMovieRepository())
    }
}