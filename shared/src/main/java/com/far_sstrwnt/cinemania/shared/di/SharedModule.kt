package com.far_sstrwnt.cinemania.shared.di

import com.far_sstrwnt.cinemania.shared.data.datasource.media.MediaRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.api.TmdbService
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
    fun provideIoDispatcher() = Dispatchers.IO
}