package com.far_sstrwnt.cinemania.ui.peopledetail

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class PeopleDetailModule {

    @ContributesAndroidInjector
    internal abstract fun contributePeopleDetailFragment(): PeopleDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(PeopleDetailViewModel::class)
    abstract fun bindPeopleDetailViewModel(viewModel: PeopleDetailViewModel): ViewModel
}