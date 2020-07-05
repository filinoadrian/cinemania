package com.far_sstrwnt.cinemania.ui.newsearch

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class NewSearchModule {

    @ContributesAndroidInjector
    internal abstract fun contributeNewSearchFragment(): NewSearchFragment

    @Binds
    @IntoMap
    @ViewModelKey(NewSearchViewModel::class)
    abstract fun bindNewSearchViewModel(viewModel: NewSearchViewModel): ViewModel
}