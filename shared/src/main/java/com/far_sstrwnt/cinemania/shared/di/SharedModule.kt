package com.far_sstrwnt.cinemania.shared.di

import com.far_sstrwnt.cinemania.shared.data.datasource.local.CinemaniaDatabase
import com.far_sstrwnt.cinemania.shared.data.datasource.local.MediaDao
import com.far_sstrwnt.cinemania.shared.data.datasource.local.MediaLocalDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.media.MediaRemoteDataSource
import com.far_sstrwnt.cinemania.shared.data.datasource.remote.api.TmdbService
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
    fun provideMediaLocalDataSource(
        database: CinemaniaDatabase,
        ioDispatcher: CoroutineDispatcher
    ): MediaLocalDataSource {
        return MediaLocalDataSource(database.mediaDao(), ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideMediaRepository(
        localDataSource: MediaLocalDataSource,
        remoteDataSource: MediaRemoteDataSource,
        ioDispatcher: CoroutineDispatcher
    ) : MediaRepository {
        return MediaRepository(localDataSource, remoteDataSource, ioDispatcher)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}