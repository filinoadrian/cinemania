package com.far_sstrwnt.cinemania.ui.media

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MediaModule {

    @ContributesAndroidInjector
    internal abstract fun contributeMediaFragment(): MediaFragment

    @Binds
    @IntoMap
    @ViewModelKey(MediaViewModel::class)
    abstract fun bindMediaViewModel(viewModel: MediaViewModel): ViewModel
}