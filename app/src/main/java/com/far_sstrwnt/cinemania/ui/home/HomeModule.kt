package com.far_sstrwnt.cinemania.ui.home

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {

    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}