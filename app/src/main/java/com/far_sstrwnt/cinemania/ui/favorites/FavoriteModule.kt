package com.far_sstrwnt.cinemania.ui.favorites

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FavoriteModule {

    @ContributesAndroidInjector
    internal abstract fun contributeFavoriteFragment(): FavoriteFragment

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun bindFavoriteViewModel(viewModel: FavoriteViewModel): ViewModel
}