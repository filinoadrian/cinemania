package com.far_sstrwnt.cinemania.shared.di

import com.far_sstrwnt.cinemania.shared.data.datasource.media.MediaRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.movie.MovieRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
import com.far_sstrwnt.cinemania.shared.data.datasource.people.PeopleRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.tv.TvRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.repository.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class SharedModule {

    @Singleton
    @Provides
    fun provideMediaRemoteDataSource(
        service: TmdbService,
        ioDispatcher: CoroutineDispatcher
    ): MediaRemoteDataSource {
        return MediaRemoteDataSource(service, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideMediaRepository(
        dataSource: MediaRemoteDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : MediaRepository {
        return MediaRepository(dataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(
        service: TmdbService,
        ioDispatcher: CoroutineDispatcher
    ): MovieRemoteDataSource {
        return MovieRemoteDataSource(service, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideMovieRepository(
        dataSource: MovieRemoteDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : MovieRepository {
        return DefaultMovieRepository(dataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideTvRemoteDataSource(
        service: TmdbService,
        ioDispatcher: CoroutineDispatcher
    ): TvRemoteDataSource {
        return TvRemoteDataSource(service, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideTvRepository(
        dataSource: TvRemoteDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : TvRepository {
        return DefaultTvRepository(dataSource, ioDispatcher)
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
    fun provideIoDispatcher() = Dispatchers.IO
}