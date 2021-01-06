package com.far_sstrwnt.cinemania

import com.far_sstrwnt.cinemania.shared.data.repository.MediaRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestApplicationModule {

    @Singleton
    @Provides
    fun provideRepository(): MediaRepository = com.far_sstrwnt.cinemania.FakeRepository()
}