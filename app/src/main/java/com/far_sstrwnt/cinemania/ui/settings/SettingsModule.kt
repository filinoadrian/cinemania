package com.far_sstrwnt.cinemania.ui.settings

import androidx.lifecycle.ViewModel
import com.far_sstrwnt.cinemania.shared.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @ContributesAndroidInjector
    internal abstract fun contributeSettingsFragment(): SettingsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel
}