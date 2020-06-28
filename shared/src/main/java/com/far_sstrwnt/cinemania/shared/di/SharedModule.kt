package com.far_sstrwnt.cinemania.shared.di

import com.far_sstrwnt.cinemania.shared.data.datasource.MovieRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.repository.DefaultMovieRepository
import com.far_sstrwnt.cinemania.shared.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedModule {

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(service: TmdbService): MovieRemoteDataSource {
        return MovieRemoteDataSource(service)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        movieRemoteDataSource: MovieRemoteDataSource
    ) : MovieRepository {
        return DefaultMovieRepository(movieRemoteDataSource)
    }
}