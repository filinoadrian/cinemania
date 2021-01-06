package com.far_sstrwnt.cinemania.ui.mediadetail

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MediaDetailModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMediaDetailFragment(): MediaDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(MediaDetailViewModel::class)
    abstract fun bindMediaDetailViewModel(viewModel: MediaDetailViewModel): ViewModel
}