package com.far_sstrwnt.cinemania.di

import com.far_sstrwnt.cinemania.shared.di.ActivityScoped
import com.far_sstrwnt.cinemania.ui.MainActivity
import com.far_sstrwnt.cinemania.ui.search.SearchModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            SearchModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}