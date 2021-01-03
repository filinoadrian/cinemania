package com.far_sstrwnt.cinemania

import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import com.far_sstrwnt.cinemania.di.DaggerAppComponent
import com.far_sstrwnt.cinemania.shared.data.repository.PreferenceRepository
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

open class MainApplication : DaggerApplication() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            enableStrictMode()
            Timber.plant(Timber.DebugTree())
        }
        super.onCreate()
        preferenceRepository = PreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
        )
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"
    }
}