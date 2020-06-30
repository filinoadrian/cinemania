package com.far_sstrwnt.cinemania.ui.tv

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TvModule {

    @ContributesAndroidInjector
    internal abstract fun contributeTvFragment(): TvFragment

    @Binds
    @IntoMap
    @ViewModelKey(TvViewModel::class)
    abstract fun bindTvViewModel(viewModel: TvViewModel): ViewModel
}