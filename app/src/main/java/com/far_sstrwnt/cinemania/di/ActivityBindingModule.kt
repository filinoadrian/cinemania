package com.far_sstrwnt.cinemania.di

import com.far_sstrwnt.cinemania.shared.di.ActivityScoped
import com.far_sstrwnt.cinemania.ui.MainActivity
import com.far_sstrwnt.cinemania.ui.home.HomeModule
import com.far_sstrwnt.cinemania.ui.media.MediaModule
import com.far_sstrwnt.cinemania.ui.moviedetail.MovieDetailModule
import com.far_sstrwnt.cinemania.ui.search.SearchModule
import com.far_sstrwnt.cinemania.ui.peopledetail.PeopleDetailModule
import com.far_sstrwnt.cinemania.ui.tvdetail.TvDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
@Suppress("UNUSED")
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            HomeModule::class,
            MediaModule::class,
            SearchModule::class,
            MovieDetailModule::class,
            TvDetailModule::class,
            PeopleDetailModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}