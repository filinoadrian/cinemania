package com.far_sstrwnt.cinemania.shared.di

import com.far_sstrwnt.cinemania.shared.data.datasource.movie.MovieRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.people.PeopleRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.tv.TvRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedModule {

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(service: TmdbService): MovieRemoteDataSource {
        return MovieRemoteDataSource(
            service
        )
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        dataSource: MovieRemoteDataSource
    ) : MovieRepository {
        return DefaultMovieRepository(dataSource)
    }

    @Singleton
    @Provides
    fun providePeopleRemoteDataSource(service: TmdbService): PeopleRemoteDataSource {
        return PeopleRemoteDataSource(
            service
        )
    }

    @Singleton
    @Provides
    fun providePeopleRepository(
        dataSource: PeopleRemoteDataSource
    ) : PeopleRepository {
        return DefaultPeopleRepository(dataSource)
    }

    @Singleton
    @Provides
    fun provideTvRemoteDataSource(service: TmdbService): TvRemoteDataSource {
        return TvRemoteDataSource(
            service
        )
    }

    @Singleton
    @Provides
    fun provideTvRepository(
        dataSource: TvRemoteDataSource
    ) : TvRepository {
        return DefaultTvRepository(dataSource)
    }
}