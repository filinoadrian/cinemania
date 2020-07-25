package com.far_sstrwnt.cinemania.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.far_sstrwnt.cinemania.model.MovieEntity
import com.far_sstrwnt.cinemania.model.TvEntity
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieNowPlayingUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMoviePopularUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieTopRatedUseCase
import com.far_sstrwnt.cinemania.shared.domain.movie.FetchMovieUpcomingUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvAiringTodayUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvOnTheAirUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvPopularUseCase
import com.far_sstrwnt.cinemania.shared.domain.tv.FetchTvTopRatedUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val fetchMovieNowPlayingUseCase: FetchMovieNowPlayingUseCase,
    private val fetchMovieUpcomingUseCase: FetchMovieUpcomingUseCase,
    private val fetchMoviePopularUseCase: FetchMoviePopularUseCase,
    private val fetchMovieTopRatedUseCase: FetchMovieTopRatedUseCase,
    private val fetchTvAiringTodayUseCase: FetchTvAiringTodayUseCase,
    private val fetchTvOnTheAirUseCase: FetchTvOnTheAirUseCase,
    private val fetchTvPopularUseCase: FetchTvPopularUseCase,
    private val fetchTvTopRatedUseCase: FetchTvTopRatedUseCase
) : ViewModel() {

    fun nowPlayingMovie(): Flow<PagingData<MovieEntity>> {
        return fetchMovieNowPlayingUseCase.execute().cachedIn(viewModelScope)
    }

    fun upcomingMovie(): Flow<PagingData<MovieEntity>> {
        return fetchMovieUpcomingUseCase.execute().cachedIn(viewModelScope)
    }

    fun popularMovie(): Flow<PagingData<MovieEntity>> {
        return fetchMoviePopularUseCase.execute().cachedIn(viewModelScope)
    }

    fun topRatedMovie(): Flow<PagingData<MovieEntity>> {
        return fetchMovieTopRatedUseCase.execute().cachedIn(viewModelScope)
    }

    fun airingTodayTv(): Flow<PagingData<TvEntity>> {
        return fetchTvAiringTodayUseCase.execute().cachedIn(viewModelScope)
    }

    fun onTheAirTV(): Flow<PagingData<TvEntity>> {
        return fetchTvOnTheAirUseCase.execute().cachedIn(viewModelScope)
    }

    fun popularTv(): Flow<PagingData<TvEntity>> {
        return fetchTvPopularUseCase.execute().cachedIn(viewModelScope)
    }

    fun topRatedTv(): Flow<PagingData<TvEntity>> {
        return fetchTvTopRatedUseCase.execute().cachedIn(viewModelScope)
    }
}