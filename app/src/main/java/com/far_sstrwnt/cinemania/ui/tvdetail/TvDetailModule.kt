package com.far_sstrwnt.cinemania.ui.tvdetail

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TvDetailModule {

    @ContributesAndroidInjector
    internal abstract fun contributeTvDetailFragment(): TvDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(TvDetailViewModel::class)
    abstract fun bindTvDetailViewModel(viewModel: TvDetailViewModel): ViewModel
}