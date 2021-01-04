package com.far_sstrwnt.cinemania

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestMainApplication : MainApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestApplicationComponent.factory().create(this)
    }
}