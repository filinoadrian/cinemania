package com.far_sstrwnt.cinemania.di

import com.far_sstrwnt.cinemania.shared.di.ActivityScoped
import com.far_sstrwnt.cinemania.ui.MainActivity
import com.far_sstrwnt.cinemania.ui.home.HomeModule
import com.far_sstrwnt.cinemania.ui.moviedetail.MovieDetailModule
import com.far_sstrwnt.cinemania.ui.movies.MoviesModule
import com.far_sstrwnt.cinemania.ui.search.SearchModule
import com.far_sstrwnt.cinemania.ui.peopledetail.PeopleDetailModule
import com.far_sstrwnt.cinemania.ui.tv.TvModule
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
            MoviesModule::class,
            SearchModule::class,
            MovieDetailModule::class,
            TvModule::class,
            TvDetailModule::class,
            PeopleDetailModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}