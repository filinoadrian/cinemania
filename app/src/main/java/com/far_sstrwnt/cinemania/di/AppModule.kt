package com.far_sstrwnt.cinemania.di

import android.content.Context
import com.far_sstrwnt.cinemania.MainApplication
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: MainApplication): Context {
        return application.applicationContext
    }
}