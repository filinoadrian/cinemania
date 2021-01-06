package com.far_sstrwnt.cinemania.shared.di

import android.content.Context
import androidx.room.Room
import com.far_sstrwnt.cinemania.shared.data.datasource.local.CinemaniaDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): CinemaniaDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CinemaniaDatabase::class.java,
            "Cinemania.db"
        ).build()
    }
}